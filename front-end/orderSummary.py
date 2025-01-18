import tkinter as tk
from tkinter import ttk

class OrderSummary(tk.Frame):
    def __init__(self, master, manager, order, table_name):
        super().__init__(master)
        self.manager = manager
        self.order = order
        self.table_name = table_name
        self.create_widgets()

    def create_widgets(self):
        self.grid_rowconfigure(0, weight=1)
        self.grid_rowconfigure(1, weight=10)
        self.grid_columnconfigure(0, weight=1)

        tk.Label(self, text=f"Order Summary - {self.table_name}", font=("Arial", 24)).grid(row=0, column=0, pady=20)

        order_details_frame = tk.Frame(self)
        order_details_frame.grid(row=1, column=0, padx=20, pady=20, sticky="nsew")

        tk.Label(order_details_frame, text="Items in Order:", font=("Arial", 16)).pack(anchor="w", pady=10)
        order_table = ttk.Treeview(order_details_frame, columns=("Name", "Quantity", "Price"), show="headings")
        order_table.heading("Name", text="Name")
        order_table.heading("Quantity", text="Quantity")
        order_table.heading("Price", text="Price")
        order_table.pack(fill="both", expand=True)

        total_price = 0
        for meal in self.order["meals"]:
            order_table.insert("", "end", values=(meal["name"], meal["quantity"], f"{meal['price']} PLN"))
            total_price += meal["quantity"] * meal["price"]

        tk.Label(self, text=f"Total: {total_price} PLN", font=("Arial", 20)).grid(row=2, column=0, pady=20)

        button_frame = tk.Frame(self)
        button_frame.grid(row=3, column=0, pady=20)

        tk.Button(button_frame, text="Pay with Card", font=("Arial", 16), bg="blue", fg="white",
                  command=self.redirect_to_tables).pack(side="left", padx=20)
        tk.Button(button_frame, text="Pay with Cash", font=("Arial", 16), bg="green", fg="white",
                  command=self.redirect_to_tables).pack(side="right", padx=20)

    def redirect_to_tables(self):
        self.manager.switch_to("TabletopEditor")
