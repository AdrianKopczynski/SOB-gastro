import tkinter as tk
from tkinter import ttk, messagebox
import json
from orderEditor import OrderEditor
import os

from requestHandler import RequestHandler as rh
from urllib.error import HTTPError

from datetime import datetime
from datetime import timezone

try:
    with open('config.json', "r", encoding="utf-8") as file:
        config = json.load(file)
        db = rh(config[0]['connection_addr'])
except FileNotFoundError:
    print("Cannot get connection adress!")


class TabletopDashboard(tk.Frame):
    def __init__(self, master, manager, table_name, table_id):
        super().__init__(master)
        self.manager = manager
        self.table_id = table_id
        self.table_name = table_name if table_name else "No Table"
        self.username = self.manager.username
        self.user = rh.get_user_by_username(db,self.username)
        self.userId = self.user["id"]
        self.userType = self.user["userType"]
        self.allOrders = rh.get_all_orders(db)
        #print(self.userId)
        self.create_widgets()

    def create_widgets(self):
        self.grid_rowconfigure(0, weight=1)
        self.grid_rowconfigure(1, weight=10)
        self.grid_columnconfigure(0, weight=1)
        self.grid_columnconfigure(1, weight=4)

        tk.Label(self, text=f"{self.table_name}", font=("Arial", 24)).grid(row=0, column=0, columnspan=2, pady=20)

        buttons_frame = tk.Frame(self)
        buttons_frame.grid(row=1, column=0, sticky="nsew", padx=10, pady=10)

        actions = [
            ("Back", lambda: self.manager.switch_to("TabletopEditor")),
            ("New Order", self.create_order),
            ("Edit Order", self.edit_order),
            ("Close Order", self.close_order),     
        ]

        buttons_frame.grid_rowconfigure(0, weight=1)
        buttons_frame.grid_rowconfigure(len(actions) + 1, weight=1)
        buttons_frame.grid_columnconfigure(0, weight=1)
        buttons_frame.grid_columnconfigure(2, weight=1)

        for idx, (label, command) in enumerate(actions):
            tk.Button(buttons_frame, text=label, font=("Arial", 16), bg="gray", fg="white",
                      width=15, height=2, command=command).grid(row=idx + 1, column=1, pady=10)
            
        if self.userType == "Admin":
            tk.Button(buttons_frame, text="Delete Order", font=("Arial", 16), bg="gray", fg="white",
                      width=15, height=2, command=self.delete_order).grid(row=5, column=1, pady=10)

        style = ttk.Style()
        style.configure("Treeview", font=("Arial", 14), rowheight=40)
        style.configure("Treeview.Heading", font=("Arial", 16, "bold"))

        orders_frame = tk.Frame(self)
        orders_frame.grid(row=1, column=1, sticky="nsew", padx=10, pady=10)

        tk.Label(orders_frame, text="Active Orders (Tables)", font=("Arial", 18)).pack(pady=10)

        self.orders_list = ttk.Treeview(orders_frame, columns=("Order ID", "Created At", "Comment", "Status"),
                                        show="headings")
        self.orders_list.heading("Order ID", text="Order ID")
        self.orders_list.heading("Created At", text="Created At")
        self.orders_list.heading("Comment", text="Comment")
        self.orders_list.heading("Status", text="Status")
        self.orders_list.pack(fill="both", expand=True)

        self.get_orders_by_table()

    def get_orders_by_table(self):
        self.orders_list.delete(*self.orders_list.get_children())
        
        try:
            data = rh.get_all_orders(db)
            #print(data)
            self.orders_list.delete(*self.orders_list.get_children())
            for order in data:
                createdAt = datetime.fromisoformat(
                    order['createdAt'][:-1]
                ).astimezone(timezone.utc)
                createdAt.strftime('%Y-%m-%d %H:%M:%S')
                self.orders_list.insert(
                    "", "end", values=(order['id'], createdAt ,order["comment"],"Zamknięte" if order['closed'] else "Otwarte")
                )
        except TypeError as err:
            print(f"Error while loading meals, error message: {err}")
            self.orders_list = []

        self.current_orders = [order for order in data if order["tabletop"] == self.table_id]
        self.orders_list.delete(*self.orders_list.get_children())
        for order in self.current_orders:
            if order["user"]["id"] == self.userId:
                self.orders_list.insert(
                        "", "end", values=(order['id'],createdAt,order["comment"],"Zamknięte" if order['closed'] else "Otwarte")
                    )

    def create_order(self):
        self.manager.current_table_id = self.table_id
        self.manager.switch_to("OrderEditor", table_name=self.table_name, tableId = self.table_id)

    def edit_order(self):
        selected_item = self.orders_list.selection()
        if not selected_item:
            messagebox.showwarning("Warning", "No order selected.")
            return

        order_data = self.orders_list.item(selected_item)["values"]
        order_id = order_data[0]

        all_orders = rh.get_all_orders(db)

        selected_order = next((order for order in all_orders if order["id"] == order_id), None)
        if not selected_order:
            messagebox.showerror("Error", "Order not found.")
            return

        self.manager.current_table_id = self.table_id
        self.manager.switch_to("OrderEditor", order=selected_order, table_name=self.table_name, tableId = self.table_id)

    def delete_order(self):
        selected_item = self.orders_list.selection()
        if not selected_item:
            messagebox.showwarning("Warning", "No order selected.")
            return

        order_id = self.orders_list.item(selected_item)["values"][0]

        if messagebox.askyesno("Confirm", f"Are you sure you want to delete order {order_id}?"):
            try:
                rh.delete_order(db,order_id)
            except HTTPError as e:
                print(f"Error: {e}")

    def close_order(self):
        selected_item = self.orders_list.selection()
        if not selected_item:
            messagebox.showwarning("Warning", "No order selected.")
            return

        order_data = self.orders_list.item(selected_item)["values"]
        order_id = order_data[0]

        selected_order = [order for order in self.allOrders if order["id"] == order_id]
        if not selected_order:
            messagebox.showerror("Error", "Order not found.")
            return

        """if "meals" not in selected_order:
            selected_order["meals"] = []"""

        self.manager.switch_to("OrderSummary", order=selected_order, table_name=self.table_name)
