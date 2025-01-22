import tkinter as tk
from tkinter import ttk, messagebox, simpledialog
from requestHandler import RequestHandler as rh
import json
import os
from urllib.error import HTTPError

base_dir = os.path.dirname(os.path.abspath(__file__)) #poki operujemy na plikach to dodaje bo jesli uzywas vscode a pycharma to inaczej pliki moze struturyzowac
file_path = os.path.join(base_dir, 'users2.json')
try:
    with open('config.json', "r", encoding="utf-8") as file:
        config = json.load(file)
        db = rh(config[0]['connection_addr'])
except FileNotFoundError:
    print("Cannot get connection adress!")


class AdminPanel(tk.Frame):
    def __init__(self, master, manager, request_handler):
        super().__init__(master)
        self.manager = manager
        self.request_handler = request_handler
        self.categories = []
        self.meals = []

        self.grid_rowconfigure(0, weight=0)
        self.grid_rowconfigure(1, weight=1) 
        self.grid_columnconfigure(0, weight=1)
        self.create_navbar()
        self.create_layout()
        
    def create_navbar(self):
        self.navbar_frame = tk.Frame(self, bg="lightgray")
        self.navbar_frame.grid(row=0, column=0, sticky="ew")
        self.navbar_frame.grid_columnconfigure(0, weight=1)

        style = ttk.Style()
        style.configure("TButton", background="lightgray", foreground="black", font=("Arial", 15), padding=5)
        style.map("TButton", background=[("active", "darkgray")])

        self.main_tab_button = ttk.Button(self.navbar_frame, text="Główna", command=lambda: self.switch_tab("main"))
        self.main_tab_button.grid(row=0, column=0, sticky="w", padx=5)

        self.user_tab_button = ttk.Button(self.navbar_frame, text="Edycja użytkowników", command=lambda: self.switch_tab("user"))
        self.user_tab_button.grid(row=0, column=1, sticky="w", padx=5)

        self.category_meal_tab_button = ttk.Button(self.navbar_frame, text="Kategorie i Posiłki", command=lambda: self.switch_tab("category_meal"))
        self.category_meal_tab_button.grid(row=0, column=2, sticky="w", padx=5)

    def create_layout(self):

        self.main_tab = tk.Frame(self)
        self.user_tab = tk.Frame(self)
        self.category_meal_tab = tk.Frame(self)

        self.tabs = {
            "main": self.main_tab,
            "user": self.user_tab,
            "category_meal": self.category_meal_tab,
        }

        self.create_main_tab()
        self.create_user_tab()
        self.create_category_meal_tab()


        self.switch_tab("main")

    def switch_tab(self, tab_name):
        for tab in self.tabs.values():
            tab.grid_forget()
        self.tabs[tab_name].grid(row=1, column=0, sticky="nsew")

    def create_main_tab(self):
        self.user_tab.grid_rowconfigure(0, weight=1)
        self.user_tab.grid_columnconfigure(0, weight=1)
        tk.Label(self.main_tab, text="Panel Admina - Główna", font=("Arial", 24)).pack(pady=20)
        tk.Label(self.main_tab, text="Witaj w panelu administracyjnym!", font=("Arial", 14)).pack(pady=10)

    def create_user_tab(self):
        self.create_user_tab_view(self.user_tab)

    def create_user_tab_view(self, frame):
        frame.grid_columnconfigure(0, weight=1)
        frame.grid_columnconfigure(1, weight=3)
        frame.grid_columnconfigure(2, weight=1, minsize=300)
        frame.grid_rowconfigure(0, weight=0)
        frame.grid_rowconfigure(1, weight=1)
        
        buttons_frame = tk.Frame(frame)
        buttons_frame.grid(row=1, column=0, sticky="nsew", padx=10, pady=10)

        actions = [
            ("Dodaj użytkownika", self.show_add_user_form),
            ("Edytuj użytkownika", self.show_edit_user_form),
            ("Usuń użytkownika", self.delete_user),
        ]

        for label, command in actions.items():
            tk.Button(buttons_frame, text=label, font=("Arial", 16), bg="gray", fg="white",
                    width=15, height=2, command=command).pack(pady=10)

        users_frame = tk.Frame(frame)
        users_frame.grid(row=1, column=1, sticky="nsew", padx=10, pady=10)

        tk.Label(users_frame, text="Lista użytkowników", font=("Arial", 18)).pack(pady=10)

        style = ttk.Style()
        style.configure("Treeview", font=("Arial", 14), rowheight=40) 
        style.configure("Treeview.Heading", font=("Arial", 16, "bold"))

        self.users_listbox = ttk.Treeview(users_frame, columns=("ID", "Nazwa", "Typ", "Aktywny"), show="headings")
        self.users_listbox.heading("ID", text="ID")
        self.users_listbox.heading("Nazwa", text="Nazwa")
        self.users_listbox.heading("Typ", text="Typ")
        self.users_listbox.heading("Aktywny", text="Aktywny")

        self.users_listbox.column("ID", anchor="center", width=10)
        self.users_listbox.column("Nazwa", anchor="w", width=160)
        self.users_listbox.column("Typ", anchor="center", width=100)
        self.users_listbox.column("Aktywny", anchor="center", width=70)
        self.users_listbox.pack(fill="both", expand=True)


        self.load_users()

        self.dynamic_frame = tk.Frame(frame, bg="white", relief="sunken", bd=2, width=300)
        self.dynamic_frame.grid(row=1, column=2, sticky="nsew", padx=10, pady=10)

        buttons_frame.grid_propagate(False)
        users_frame.grid_propagate(False)  
        
    def load_users(self):
        try:
            users = rh.get_all_users(db)
        except TypeError as err:
            print(f"Error while loading users, error message: {err}")
            users = []

        self.users_listbox.delete(*self.users_listbox.get_children())
        for user in users:
            self.users_listbox.insert(
                "", "end", values=(user['id'], user['username'], user['userType'], "Tak" if user['enabled'] else "Nie")
            )
    
    def load_meals(self):
        try:
            data = rh.get_all_meals(db)
            self.meal_table.delete(*self.meal_table.get_children())
            for entry in data:
                self.meal_table.insert(
                    "", "end", values=(entry['name'], entry['price'], entry['category']["name"],)
                )
        except TypeError as err:
            print(f"Error while loading meals, error message: {err}")
            self.meal_table = []

    def load_categories(self):
        try:
            data = rh.get_categories_list(db)
            self.category_table.delete(*self.category_table.get_children())
            for entry in data:
                self.category_table.insert(
                    "", "end", values=(entry['name'],)
                )
        except TypeError as err:
            print(f"Error while loading categories, error message: {err}")
            self.category_table = []

    def show_add_user_form(self):
        """for widget in self.dynamic_frame.winfo_children():
            widget.destroy()"""
        self.clear_dynamic_frame()
        
        tk.Label(self.dynamic_frame, text="Dodaj użytkownika", font=("Arial", 18)).pack(pady=10)

        tk.Label(self.dynamic_frame, text="Nazwa użytkownika:", font=("Arial", 14)).pack(pady=5)
        username_entry = tk.Entry(self.dynamic_frame, font=("Arial", 14))
        username_entry.pack(pady=5)
    
        tk.Label(self.dynamic_frame, text="PIN (4 cyfry):", font=("Arial", 14)).pack(pady=5)
        pin_entry = tk.Entry(self.dynamic_frame, show="*", font=("Arial", 14))
        pin_entry.pack(pady=5)

        tk.Label(self.dynamic_frame, text="Typ użytkownika:", font=("Arial", 14)).pack(pady=5)
        user_type_combo = ttk.Combobox(self.dynamic_frame, values=["Cashier", "Inventory", "Admin"], font=("Arial", 14))
        user_type_combo.pack(pady=5)

        def submit():
            user_name = username_entry.get().strip()
            login_pin = pin_entry.get().strip()
            user_type = user_type_combo.get()

            if not user_name or not login_pin or not user_type:
                tk.Label(self.dynamic_frame, text="Wszystkie pola są wymagane!", fg="red", font=("Arial", 12)).pack(pady=5)
                return

            if len(login_pin) != 4 or not login_pin.isdigit():
                tk.Label(self.dynamic_frame, text="PIN musi składać się z 4 cyfr!", fg="red", font=("Arial", 12)).pack(pady=5)
                return

            # if self.request_handler.check_user_exists(user_name):
            if self.check_username_exists(user_name):
                tk.Label(self.dynamic_frame, text="Użytkownik o tej nazwie już istnieje!", fg="red", font=("Arial", 12)).pack(pady=5)
                return
            
            if self.check_pin_exists(login_pin):
                tk.Label(self.dynamic_frame, text="Pin jest już w użyciu!", fg="red", font=("Arial", 12)).pack(pady=5)
                return

            new_user = {
                "username": user_name,
                "loginPin": login_pin,
                "userType": user_type,
                "enabled": "True"
            }
            success = rh.create_user(db,new_user)

            if success:
                self.load_users()
                tk.Label(self.dynamic_frame, text="Użytkownik dodany!", fg="green", font=("Arial", 12)).pack(pady=5)
            else:
                tk.Label(self.dynamic_frame, text="Nie udało się dodać użytkownika.", fg="red", font=("Arial", 12)).pack(pady=5)
        
        tk.Button(self.dynamic_frame, text="Dodaj", font=("Arial", 14), bg="green", fg="white", command=submit).pack(pady=10)

    def delete_user(self):
        selected_item = self.users_listbox.selection()
        if selected_item:
            user_id = int(self.users_listbox.item(selected_item, "values")[0])

            self.clear_dynamic_frame()
            tk.Label(self.dynamic_frame, text=f"Czy na pewno chcesz usunąć użytkownika o ID {user_id}?",
                    font=("Arial", 12), fg="red").pack(pady=10)

            def confirm_deletion():
                try:
                    """with open(file_path, 'r', encoding='utf-8') as f:
                        users = json.load(f)

                    updated_users = [user for user in users if user["user_id"] != user_id]

                    with open(file_path, 'w', encoding='utf-8') as f:
                        json.dump(updated_users, f, indent=4)"""
                    rh.delete_user_by_id(db, user_id)

                    self.load_users()

                    self.clear_dynamic_frame()
                    success_label = tk.Label(self.dynamic_frame, text=f"Użytkownik o ID {user_id} został usunięty.",
                                            font=("Arial", 12), fg="green")
                    success_label.pack(pady=10)

                    self.after(3000, lambda: success_label.destroy())

                except FileNotFoundError:
                    error_label = tk.Label(self.dynamic_frame, text="Plik z użytkownikami nie istnieje.",
                                        font=("Arial", 12), fg="red")
                    error_label.pack(pady=10)
                    self.after(3000, lambda: error_label.destroy())

            tk.Button(self.dynamic_frame, text="Usun", command=confirm_deletion,
                    fg="white", bg="red", font=("Arial", 12)).pack(pady=10)

        else:
            self.clear_dynamic_frame()
            warning_label = tk.Label(self.dynamic_frame, text="Proszę wybrać użytkownika do usunięcia.",
                                    fg="red", font=("Arial", 12))
            warning_label.pack(pady=5)
            self.after(3000, lambda: warning_label.destroy())

            
    def show_edit_user_form(self):
        selected_item = self.users_listbox.selection()
        if not selected_item:
            messagebox.showwarning("Brak wyboru", "Proszę wybrać użytkownika do edycji.")
            return

        user_data = self.users_listbox.item(selected_item, "values")
        user_id, username, user_type = user_data[:3]

        for widget in self.dynamic_frame.winfo_children():
            widget.destroy()

        tk.Label(self.dynamic_frame, text="Edytuj użytkownika", font=("Arial", 18)).pack(pady=10)

        tk.Label(self.dynamic_frame, text="Nazwa użytkownika:", font=("Arial", 14)).pack(pady=5)
        username_entry = tk.Entry(self.dynamic_frame, font=("Arial", 14))
        username_entry.insert(0, username)
        username_entry.pack(pady=5)

        tk.Label(self.dynamic_frame, text="Typ użytkownika:", font=("Arial", 14)).pack(pady=5)
        user_type_combo = ttk.Combobox(self.dynamic_frame, values=["Cashier", "Inventory", "Admin"], font=("Arial", 14))
        user_type_combo.set(user_type)
        user_type_combo.pack(pady=5)

        def submit():
            new_username = username_entry.get().strip()
            new_user_type = user_type_combo.get()

            if not new_username or not new_user_type:
                messagebox.showerror("Błąd", "Wszystkie pola są wymagane!")
                return

            try:
                with open("users2.json", "r", encoding="utf-8") as f:
                    users = json.load(f)

                for user in users:
                    if user["id"] == int(user_id):
                        user["username"] = new_username
                        user["userType"] = new_user_type
                        break

                with open("users2.json", "w", encoding="utf-8") as f:
                    json.dump(users, f, indent=4)

                self.load_users()
                messagebox.showinfo("Sukces", "Użytkownik został zaktualizowany.")

            except FileNotFoundError:
                messagebox.showerror("Błąd", "Plik u nie istnieje.")

        tk.Button(self.dynamic_frame, text="Zapisz", font=("Arial", 14), bg="green", fg="white", command=submit).pack(pady=10)

    def clear_dynamic_frame(self):
        for widget in self.dynamic_frame.winfo_children():
            widget.destroy()

    def check_username_exists(self, userName):
        users = rh.get_all_users(db)
        for user in users:
            if user["username"] == userName:
                return True
        return False
    
    def check_pin_exists(self, loginPin):
        users = rh.get_all_users(db)
        for user in users:
            if user["loginPin"] == loginPin:
                return True
        return False
    
    def create_category_meal_tab(self):
        self.category_meal_tab.grid_rowconfigure(0, weight=1)
        self.category_meal_tab.grid_columnconfigure(0, weight=1)
        self.category_meal_tab.grid_columnconfigure(1, weight=3)
        self.category_meal_tab.grid_columnconfigure(2, weight=1)

        buttons_frame = tk.Frame(self.category_meal_tab)
        buttons_frame.grid(row=0, column=0, sticky="ns", padx=10, pady=10)

        tk.Button(buttons_frame, text="Dodaj Kategorię", font=("Arial", 14), command=self.show_add_category_form).pack(fill="x", pady=10)
        tk.Button(buttons_frame, text="Edytuj Kategorię", font=("Arial", 14), command=self.show_edit_category_form).pack(fill="x", pady=10)
        tk.Button(buttons_frame, text="Usuń Kategorię", font=("Arial", 14), command=self.show_delete_category_confirmation).pack(fill="x", pady=10)
        tk.Button(buttons_frame, text="Dodaj Posiłek", font=("Arial", 14), command=self.show_add_meal_form).pack(fill="x", pady=10)
        tk.Button(buttons_frame, text="Edytuj Posiłek", font=("Arial", 14), command=self.show_edit_meal_form).pack(fill="x", pady=10)
        tk.Button(buttons_frame, text="Usuń Posiłek", font=("Arial", 14), command=self.show_delete_meal_confirmation).pack(fill="x", pady=10)

        lists_frame = tk.Frame(self.category_meal_tab)
        lists_frame.grid(row=0, column=1, sticky="nsew", padx=10, pady=10)

        tk.Label(lists_frame, text="Kategorie", font=("Arial", 18)).pack(pady=5)
        self.category_table = ttk.Treeview(lists_frame, columns=("Nazwa",), show="headings")
        self.category_table.heading("Nazwa", text="Nazwa")
        self.category_table.column("Nazwa", width=200, anchor="w")
        self.category_table.pack(fill="both", expand=True, pady=5)

        tk.Label(lists_frame, text="Posiłki", font=("Arial", 18)).pack(pady=5)
        self.meal_table = ttk.Treeview(lists_frame, columns=("Nazwa", "Cena", "Kategoria"), show="headings")
        self.meal_table.heading("Nazwa", text="Nazwa")
        self.meal_table.heading("Cena", text="Cena")
        self.meal_table.heading("Kategoria", text="Kategoria")
        self.meal_table.column("Nazwa", width=150, anchor="w")
        self.meal_table.column("Cena", width=100, anchor="center")
        self.meal_table.column("Kategoria", width=150, anchor="w")
        self.meal_table.pack(fill="both", expand=True, pady=5)

        self.dynamic_frame = tk.Frame(self.category_meal_tab, bg="white", relief="sunken", bd=2)
        self.dynamic_frame.grid(row=0, column=2, sticky="nsew", padx=10, pady=10)

        self.load_meals()
        self.load_categories()

    def show_add_category_form(self):
        self.clear_dynamic_frame()
        tk.Label(self.dynamic_frame, text="Dodaj Kategorię", font=("Arial", 18)).pack(pady=10)
        category_entry = tk.Entry(self.dynamic_frame, font=("Arial", 14))
        category_entry.pack(pady=5)

        def submit():
            category = category_entry.get().strip()
            if category and category not in self.categories:
                try:
                    rh.create_category(db,{"name":f"{category}"})
                except HTTPError:
                    self.display_message("Błąd", "Nie udało się dodać kategorii.")
                else:
                    self.display_message("Sukces", "Dodano kategorię.")
                    self.load_categories()
                
            else:
                self.display_message("Błąd", "Nieprawidłowa lub już istniejąca kategoria.")

        tk.Button(self.dynamic_frame, text="Dodaj", font=("Arial", 14), bg="green", fg="white", command=submit).pack(pady=10)

    def show_edit_category_form(self):
        selected_item = self.category_table.selection()
        if not selected_item:
            self.display_message("Błąd", "Wybierz kategorię do edycji.")
            return

        category_name = self.category_table.item(selected_item, "values")[0]
        self.clear_dynamic_frame()

        tk.Label(self.dynamic_frame, text="Edytuj Kategorię", font=("Arial", 18)).pack(pady=10)
        category_entry = tk.Entry(self.dynamic_frame, font=("Arial", 14))
        category_entry.insert(0, category_name)
        category_entry.pack(pady=5)

        def submit():
            new_name = category_entry.get().strip()
            if new_name not in [entry["name"] for entry in rh.get_categories_list(db)]:
                id = [entry["id"] for entry in rh.get_categories_list(db) if entry["name"] == category_name]
                try:
                    rh.update_category(db,id[0],{'name':f'{new_name}'})
                except HTTPError:
                    self.display_message("Błąd", "Nieprawidłowa lub już istniejąca kategoria.")
                else:
                    self.display_message("Sukces", "Zaktualizowano kategorię.")
                    self.load_categories()
                    self.load_meals()
            else:
                self.display_message("Błąd", "Nieprawidłowa lub już istniejąca kategoria.")

        tk.Button(self.dynamic_frame, text="Zapisz", font=("Arial", 14), bg="blue", fg="white", command=submit).pack(pady=10)

    def show_delete_category_confirmation(self):
        selected_item = self.category_table.selection()
        if not selected_item:
            self.display_message("Błąd", "Wybierz kategorię do usunięcia.")
            return

        category_name = self.category_table.item(selected_item, "values")[0]
        self.clear_dynamic_frame()
        id = [entry["id"] for entry in rh.get_categories_list(db) if entry["name"] == category_name]
        tk.Label(self.dynamic_frame, text=f"Czy na pewno chcesz usunąć kategorię '{category_name}'?", font=("Arial", 14), fg="red").pack(pady=10)

        def confirm():
            if category_name == "BRAK":
                self.display_message("Błąd", "Nie można usunąć tej kategorii.")
            else:
                try:
                    rh.delete_category(db,id[0])
                except HTTPError:
                    self.display_message("Błąd", "Nie udało się usunać kategorii.")
                else:
                    self.display_message("Sukces", "Kategoria została usunięta.")
                    self.load_categories()
                    self.load_meals()

        tk.Button(self.dynamic_frame, text="Tak", font=("Arial", 14), bg="green", fg="white", command=confirm).pack(padx=10, pady=10)
        tk.Button(self.dynamic_frame, text="Nie", font=("Arial", 14), bg="red", fg="white", command=self.clear_dynamic_frame).pack(padx=10, pady=10)

    def show_add_meal_form(self):
        self.clear_dynamic_frame()
        tk.Label(self.dynamic_frame, text="Dodaj Posiłek", font=("Arial", 18)).pack(pady=10)
        meal_name_entry = tk.Entry(self.dynamic_frame, font=("Arial", 14))
        meal_name_entry.pack(pady=5)
        tk.Label(self.dynamic_frame, text="Cena (PLN)", font=("Arial", 14)).pack(pady=5)
        meal_price_entry = tk.Entry(self.dynamic_frame, font=("Arial", 14))
        meal_price_entry.pack(pady=5)
        tk.Label(self.dynamic_frame, text="Kategoria", font=("Arial", 14)).pack(pady=5)
        meal_category_combo = ttk.Combobox(self.dynamic_frame, values=[entry["name"] for entry in rh.get_categories_list(db)], font=("Arial", 14))
        meal_category_combo.pack(pady=5)

        def submit():
            name = meal_name_entry.get().strip()
            price = meal_price_entry.get().strip()
            category = meal_category_combo.get().strip()
            if not name or not price or not category:
                self.display_message("Błąd", "Wszystkie pola są wymagane.")
                return
            try:
                price = float(price)
            except ValueError:
                self.display_message("Błąd", "Cena musi być liczbą.")
                return
            category_object = [entry for entry in rh.get_categories_list(db) if entry["name"] == category]
            meal = {"name": name, "category": category_object[0], "price": price}
            try:
                rh.add_meal(db,meal)
            except HTTPError:
                self.display_message("Błąd", "Nie udało się dodać posiłku")
            else:
                self.display_message("Sukces", "Dodano posiłek.")
                self.load_meals()
            

        tk.Button(self.dynamic_frame, text="Dodaj", font=("Arial", 14), bg="green", fg="white", command=submit).pack(pady=10)

    def show_edit_meal_form(self):
        selected_item = self.meal_table.selection()
        if not selected_item:
            self.display_message("Błąd", "Wybierz posiłek do edycji.")
            return

        meal_name, meal_price, meal_category = self.meal_table.item(selected_item, "values")
        self.clear_dynamic_frame()

        tk.Label(self.dynamic_frame, text="Edytuj Posiłek", font=("Arial", 18)).pack(pady=10)
        meal_name_entry = tk.Entry(self.dynamic_frame, font=("Arial", 14))
        meal_name_entry.insert(0, meal_name)
        meal_name_entry.pack(pady=5)
        tk.Label(self.dynamic_frame, text="Cena (PLN)", font=("Arial", 14)).pack(pady=5)
        meal_price_entry = tk.Entry(self.dynamic_frame, font=("Arial", 14))
        meal_price_entry.insert(0, meal_price.replace(" PLN", ""))
        meal_price_entry.pack(pady=5)
        tk.Label(self.dynamic_frame, text="Kategoria", font=("Arial", 14)).pack(pady=5)
        meal_category_combo = ttk.Combobox(self.dynamic_frame, values=[entry["name"] for entry in rh.get_categories_list(db)], font=("Arial", 14))
        meal_category_combo.set(meal_category)
        meal_category_combo.pack(pady=5)

        id = [entry["id"] for entry in rh.get_all_meals(db) if entry["name"] == meal_name]

        def submit():
            new_name = meal_name_entry.get().strip()
            new_price = meal_price_entry.get().strip()
            new_category = meal_category_combo.get().strip()
            if not new_name or not new_price or not new_category:
                self.display_message("Błąd", "Wszystkie pola są wymagane.")
                return
            category_object = [entry for entry in rh.get_categories_list(db) if entry["name"] == new_category]
            meal = {"name": new_name, "category": category_object[0], "price": new_price}
            try:
                rh.update_meal(db,id[0],meal)
            except HTTPError:
                self.display_message("Błąd", "Nie udało się zaktualizować posiłku")
            else:
                self.display_message("Sukces", "Zaktualizowano posiłek.")
                self.load_meals()
            

        tk.Button(self.dynamic_frame, text="Zapisz", font=("Arial", 14), bg="blue", fg="white", command=submit).pack(pady=10)

    def show_delete_meal_confirmation(self):
        selected_item = self.meal_table.selection()
        if not selected_item:
            self.display_message("Błąd", "Wybierz posiłek do usunięcia.")
            return

        meal_name = self.meal_table.item(selected_item, "values")[0]
        self.clear_dynamic_frame()
        id = [entry["id"] for entry in rh.get_all_meals(db) if entry["name"] == meal_name]
        tk.Label(self.dynamic_frame, text=f"Czy na pewno chcesz usunąć posiłek '{meal_name}'?", font=("Arial", 14), fg="red").pack(pady=10)

        def confirm():
            try:
                rh.delete_meal(db,id[0])
            except HTTPError:
                self.display_message("Błąd", "Nie udało się usunać posiłku.")
            else:
                self.display_message("Sukces", "Posiłek został usunięty.")
                self.load_meals()

        tk.Button(self.dynamic_frame, text="Tak", font=("Arial", 14), bg="green", fg="white", command=confirm).pack(padx=10, pady=10)
        tk.Button(self.dynamic_frame, text="Nie", font=("Arial", 14), bg="red", fg="white", command=self.clear_dynamic_frame).pack(padx=10, pady=10)

    def display_message(self, title, message):
        self.clear_dynamic_frame()
        tk.Label(self.dynamic_frame, text=title, font=("Arial", 18), fg="blue").pack(pady=5)
        tk.Label(self.dynamic_frame, text=message, font=("Arial", 14)).pack(pady=5)

    def clear_dynamic_frame(self):
        for widget in self.dynamic_frame.winfo_children():
            widget.destroy()

