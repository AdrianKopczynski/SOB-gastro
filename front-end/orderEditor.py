import tkinter as tk
from tkinter import ttk, messagebox
import json
import os
from datetime import datetime
from requestHandler import RequestHandler as rh
from urllib.error import HTTPError

try:
    with open('config.json', "r", encoding="utf-8") as file:
        config = json.load(file)
        db = rh(config[0]['connection_addr'])
except FileNotFoundError:
    print("Cannot get connection adress!")


class OrderEditor(tk.Frame):
    def __init__(self, master, manager, order=None, table_name=None, tableId=None):
        super().__init__(master)
        self.manager = manager
        self.order = order
        self.table_name = table_name
        self.selected_meals = []
        self.tableId = tableId

        self.meals = self.load_meals()

        self.difference_positive = []
        self.difference_negative = []
        
        self.create_widgets()
        self.load_order_meals()

    def load_meals(self):
        meals = rh.get_all_meals(db)
        return meals
        

    def create_widgets(self):
        self.grid_rowconfigure(0, weight=0)  
        self.grid_rowconfigure(1, weight=0)  
        self.grid_rowconfigure(2, weight=0)  
        self.grid_rowconfigure(3, weight=1)
        self.grid_columnconfigure(0, weight=1)
        self.grid_columnconfigure(1, weight=2)

        title_text = f"Edycja Zamówienia" if self.order else "Nowe Zamówienie"
        tk.Label(self, text=title_text, font=("Arial", 24)).grid(row=0, column=0, columnspan=2, pady=10)

        comment_frame = tk.Frame(self)
        comment_frame.grid(row=1, column=0, columnspan=2, sticky="ew", padx=10, pady=10)

        tk.Label(comment_frame, text="Komentarz do zamówienia:", font=("Arial", 14)).pack(anchor="w", padx=5)
        self.comment_text = tk.Text(comment_frame, font=("Arial", 14), height=5, width=80, wrap="word")
        self.comment_text.pack(fill="x", padx=5)
        #print(self.order["comment"])
        if self.order and "comment" in self.order:
            if self.order["comment"] is None:
                self.order["comment"] = ""
            self.comment_text.delete("1.0", tk.END)
            self.comment_text.insert("1.0", self.order["comment"])

        def limit_comment(event):
            max_chars = 250
            current_text = self.comment_text.get("1.0", tk.END)
            if len(current_text) > max_chars:
                self.comment_text.delete("1.0", f"1.{max_chars}")
        self.comment_text.bind("<KeyRelease>", limit_comment)

        category_frame = tk.Frame(self)
        category_frame.grid(row=2, column=0, sticky="ew", padx=10, pady=5)

        canvas = tk.Canvas(category_frame, height=34)
        scrollbar = tk.Scrollbar(category_frame, orient="horizontal", command=canvas.xview)
        scrollable_frame = tk.Frame(canvas)

        scrollable_frame.bind(
            "<Configure>",
            lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
        )
        canvas.update_idletasks()
        canvas.configure(height=canvas.winfo_reqheight())
        
        canvas.create_window((0, 0), window=scrollable_frame, anchor="w")
        canvas.configure(xscrollcommand=scrollbar.set)
        canvas.pack(side="top", fill="x", expand=False)
        scrollbar.pack(fill="x")

        all_button = tk.Button(scrollable_frame, text="All", font=("Arial", 14), command=self.show_all_meals)
        all_button.pack(side="left", padx=5)

        for category in rh.get_categories_list(db):
            button = tk.Button(scrollable_frame, text=category["name"], font=("Arial", 14), height=1,
                   command=lambda c=category["name"]: self.filter_meals_by_category(c))
            button.pack(side="left", padx=5)

        left_frame = tk.Frame(self)
        left_frame.grid(row=3, column=0, sticky="nsew", padx=10, pady=10)

        tk.Label(left_frame, text="Dostępne posiłki:", font=("Arial", 14)).pack(anchor="w", padx=5)
        self.meal_table = ttk.Treeview(left_frame, columns=("Nazwa", "Cena"), show="headings")
        self.meal_table.heading("Nazwa", text="Nazwa")
        self.meal_table.heading("Cena", text="Cena")
        self.meal_table.column("Nazwa", width=200, anchor="w")
        self.meal_table.column("Cena", width=100, anchor="center")
        self.meal_table.pack(fill="both", expand=True, padx=5, pady=5)
        self.update_meal_table()

        right_frame = tk.Frame(self)
        right_frame.grid(row=2, rowspan=2, column=1, sticky="nsew", padx=10, pady=10)

        tk.Label(right_frame, text="Posiłki w zamówieniu:", font=("Arial", 14)).pack(anchor="w", padx=5)
        self.order_table = ttk.Treeview(right_frame, columns=("Nazwa", "Cena", "Ilość"), show="headings")
        self.order_table.heading("Nazwa", text="Nazwa")
        self.order_table.heading("Cena", text="Cena")
        self.order_table.heading("Ilość", text="Ilość")
        self.order_table.column("Nazwa", width=200, anchor="w")
        self.order_table.column("Cena", width=100, anchor="center")
        self.order_table.column("Ilość", width=50, anchor="center")
        self.order_table.pack(fill="both", expand=True, padx=5, pady=5)

        self.update_order_table()

        buttons_frame = tk.Frame(self)
        buttons_frame.grid(row=4, column=0, columnspan=2, pady=10)

        if self.order:
            tk.Button(buttons_frame, text="Zapisz Zamówienie", font=("Arial", 14), bg="green", fg="white",
                command=lambda :self.save_order(self.difference_positive,self.difference_negative)).pack(side="left", padx=10)
        else:
            tk.Button(buttons_frame, text="Dodaj Zamówienie", font=("Arial", 14), bg="green", fg="white",
                command=lambda :self.add_order()).pack(side="left", padx=10)

        tk.Button(buttons_frame, text="Anuluj", font=("Arial", 14), bg="red", fg="white",
                command=lambda: self.manager.switch_to("TabletopDashboard",
                                                        table_name=self.table_name,
                                                        table_id=self.manager.current_table_id)).pack(side="right", padx=10)
        self.order_table.bind("<ButtonRelease-1>", self.on_order_click)

    def add_order(self):
        if not self.selected_meals:
            messagebox.showwarning("Uwaga", "Zamówienie nie może być puste.")
            return

        new_comment = self.comment_text.get("1.0", tk.END).strip()
        comment = self.order["comment"] if self.order and "comment" in self.order and self.order["comment"] == new_comment else new_comment

        meals = []
        for meal in self.selected_meals:
            for x in range(meal["quantity"]):
                meals.append(meal["id"])

        user = rh.get_user_by_username(db,self.manager.username)

        order = {
            "mealID": meals,
            "user": user,
            
            "comment": comment,
            "tabletop": self.manager.current_table_id,
            "closed": False
        }
        rh.add_order(db,order)

        self.manager.switch_to("TabletopDashboard",
                            table_id=self.manager.current_table_id,
                            table_name=self.table_name)

    def on_order_click(self, event):
        selected_item = self.order_table.focus()
        if selected_item:
            meal_name = self.order_table.item(selected_item, "values")[0]
            self.remove_meal_from_order_by_name(meal_name)


    def update_meal_table(self):
        self.meal_table.delete(*self.meal_table.get_children())
        for meal in rh.get_all_meals(db):
            row_id = self.meal_table.insert("", "end", values=(meal["name"], f"{meal['price']} PLN"))
            self.meal_table.item(row_id, tags=row_id)
            self.meal_table.tag_bind(row_id, "<Button-1>", lambda event, meal_id=meal["id"]: self.add_meal_to_order(meal_id))
        

    def add_meal_to_order(self, meal_id):
        meal = next((m for m in self.meals if m["id"] == meal_id), None)
        if meal:
            for selected in self.selected_meals:
                if selected["id"] == meal_id:
                    selected["quantity"] += 1
                    self.difference_positive.append(meal_id)
                    self.update_order_table()
                    return
            self.selected_meals.append({
                "id": meal["id"],
                "name": meal["name"],
                "price": meal["price"],
                "quantity": 1
            })
            self.difference_positive.append(meal_id)
            self.update_order_table()
            #print(self.selected_meals)

    def prepare_meal_list(self,meals):
        ids = []
        for meal in meals:
            for quantity in enumerate(meal["quantity"]):
                ids.append(meal["id"])
        #print(ids)

    def remove_meal_from_order_by_name(self, meal_name):
        for selected in self.selected_meals:
            meal_id = [meal["id"] for meal in rh.get_all_meals(db) if meal["name"] == meal_name]
            if selected["name"] == meal_name:
                if selected["quantity"] > 1:
                    selected["quantity"] -= 1
                    self.difference_negative.append(meal_id[0])
                else:
                    self.selected_meals.remove(selected)
                    self.difference_negative.append(meal_id[0])
                self.update_order_table()
                return


    def update_order_table(self):
        self.order_table.delete(*self.order_table.get_children())
        for meal in self.selected_meals:
            self.order_table.insert("", "end", values=(
                meal["name"], f"{meal['price']} PLN", meal["quantity"]
            ))

    def load_order_meals(self):
        self.selected_meals = []
        if self.order:
            ids = [entry["mealId"] for entry in rh.get_order_meals(db,self.order["id"])]
            for meal in rh.get_all_meals(db):
                if meal["id"] in ids:
                    self.selected_meals.append({
                        "id": meal["id"],
                        "name": meal["name"],
                        "price": meal["price"],
                        "quantity": self.cal_meal_quantity(ids,meal["id"])
                    })
        if self.order and "comment" in self.order:
            if self.order["comment"] is None:
                self.order["comment"] == ""
            self.comment_text.delete("1.0", tk.END)
            self.comment_text.insert("1.0", self.order["comment"])

        self.update_order_table()

    def cal_meal_quantity(self,mealTable,id):
        counter = 0
        for ids in mealTable:
            if ids == id:
                counter +=1
        return counter

    def save_order(self,positive_difference,negative_difference):

        for entry in positive_difference:
            if entry in negative_difference:
                negative_difference.remove(entry)
                positive_difference.remove(entry)

        positiveData = {
            "mealID" : positive_difference
        }
        print(positiveData)
        if positive_difference:
            rh.add_order_meals(db,self.order["id"],positiveData)
        if negative_difference:
            for ids in negative_difference:
                id = [meal["id"] for meal in rh.get_order_meals(db,self.order["id"]) if ids == meal["mealId"]]
                print(id)
                rh.delete_order_meals(db,id[0])

        self.manager.switch_to("TabletopDashboard",
                            table_id=self.manager.current_table_id,
                            table_name=self.table_name)

    def show_all_meals(self):
        self.update_meal_table()

    def filter_meals_by_category(self, category):
        filtered_meals = [meal for meal in self.meals if meal["category"]["name"] == category]
        self.update_meal_table(filtered_meals)

    def update_meal_table(self, meals=None):
        self.meal_table.delete(*self.meal_table.get_children())
        meals_to_display = meals if meals else self.meals
        for meal in meals_to_display:
            #print(meal)
            row_id = self.meal_table.insert("", "end", values=(meal["name"], f"{meal['price']} PLN"))
            self.meal_table.item(row_id, tags=row_id)
            self.meal_table.tag_bind(row_id, "<Button-1>", lambda event, meal_id=meal["id"]: self.add_meal_to_order(meal_id))
