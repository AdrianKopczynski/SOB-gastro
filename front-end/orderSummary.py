import tkinter as tk
from tkinter import ttk
from requestHandler import RequestHandler as rh
import json

try:
    with open('config.json', "r", encoding="utf-8") as file:
        config = json.load(file)
        db = rh(config[0]['connection_addr'])
except FileNotFoundError:
    print("Cannot get connection adress!")

class OrderSummary(tk.Frame):
    def __init__(self, master, manager, order, table_name):
        super().__init__(master)
        self.manager = manager
        self.order = order[0]
        self.orderId = self.order["id"]
        self.table_name = table_name
        self.create_widgets()

    def create_widgets(self):
        self.grid_rowconfigure(0, weight=1)
        self.grid_rowconfigure(1, weight=10)
        self.grid_rowconfigure(2, weight=0)
        self.grid_rowconfigure(3, weight=0)
        self.grid_rowconfigure(4, weight=1)
        self.grid_columnconfigure(0, weight=1)
        self.grid_columnconfigure(1, weight=3)
        self.grid_columnconfigure(2, weight=1)

        tk.Label(self, text=f"Order Summary - {self.table_name}", font=("Arial", 24)).grid(row=0, column=1, pady=20)

        order_details_frame = tk.Frame(self, bg="white", relief="sunken", bd=2)
        order_details_frame.grid(row=1, column=1, padx=20, pady=20, sticky="nsew")

        scrollbar = tk.Scrollbar(order_details_frame, orient="vertical")
        scrollbar.pack(side="right", fill="y")

        order_table = ttk.Treeview(
            order_details_frame,
            columns=("Name", "Quantity", "Price"),
            show="headings",
            yscrollcommand=scrollbar.set,
            height=10
        )
        order_table.heading("Name", text="Name")
        order_table.heading("Quantity", text="Quantity")
        order_table.heading("Price", text="Price")
        order_table.column("Name", width=200, anchor="w")
        order_table.column("Quantity", width=100, anchor="center")
        order_table.column("Price", width=100, anchor="center")
        order_table.pack(fill="both", expand=True)
        scrollbar.config(command=order_table.yview)

        total_price = 0
        ids = [entry["mealId"] for entry in rh.get_order_meals(db,self.orderId)]
        meals = [meal for meal in rh.get_all_meals(db) if meal["id"] in ids]
        if len(ids) > 0:
            for meal in meals:
                order_table.insert("", "end", values=(meal["name"], meal.get("quantity", 1), f"{meal['price']} PLN"))
                total_price += meal.get("quantity", 1) * meal["price"]
        else:
            tk.Label(order_details_frame, text="No meals in this order.", font=("Arial", 14), fg="red").pack()

        tk.Label(self, text=f"Total: {total_price:.2f} PLN", font=("Arial", 20)).grid(row=2, column=1, pady=20)

        button_frame = tk.Frame(self)
        button_frame.grid(row=3, column=1, pady=10)

        tk.Button(button_frame, text="Pay with Card", font=("Arial", 16), bg="blue", fg="white",
                command=self.redirect_to_tables).pack(side="left", padx=40)
        tk.Button(button_frame, text="Pay with Cash", font=("Arial", 16), bg="green", fg="white",
                command=self.redirect_to_tables).pack(side="right", padx=40)

        spacer_frame = tk.Frame(self)
        spacer_frame.grid(row=4, column=1, sticky="nsew")

    def redirect_to_tables(self):
        rh.close_order(db,self.orderId)
        self.manager.switch_to("TabletopEditor")
