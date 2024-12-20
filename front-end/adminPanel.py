import tkinter as tk
from tkinter import ttk, messagebox, simpledialog
from requestHandler import RequestHandler

class AdminPanel(tk.Frame):
    def __init__(self, master, manager, request_handler):
        super().__init__(master)
        self.manager = manager
        self.request_handler = request_handler
        self.grid_rowconfigure(0, weight=0)
        self.grid_rowconfigure(1, weight=1) 
        self.grid_columnconfigure(0, weight=1)
        self.create_navbar()
        self.create_layout()

    def create_navbar(self):
        self.navbar_frame = tk.Frame(self, bg="lightgray")
        self.navbar_frame.grid(row=0, column=0, sticky="ew")
        self.navbar_frame.grid_columnconfigure(0, weight=1)  # Rozciągnięcie na całą szerokość

        style = ttk.Style()
        style.configure("TButton", background="lightgray", foreground="black", font=("Arial", 15), padding=5)
        style.map("TButton", background=[("active", "darkgray")])

        self.main_tab_button = ttk.Button(self.navbar_frame, text="Główna", command=lambda: self.switch_tab("main"))
        self.main_tab_button.grid(row=0, column=0, sticky="w", padx=5)

        self.user_tab_button = ttk.Button(self.navbar_frame, text="Edycja użytkowników", command=lambda: self.switch_tab("user"))
        self.user_tab_button.grid(row=0, column=1, sticky="w", padx=5)

    def create_layout(self):

        self.main_tab = tk.Frame(self)
        self.user_tab = tk.Frame(self)

        self.tabs = {
            "main": self.main_tab,
            "user": self.user_tab
        }

        self.create_main_tab()
        self.create_user_tab()

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
        frame.grid_columnconfigure(2, weight=0, minsize=300)
        frame.grid_rowconfigure(1, weight=1)

        frame.grid_rowconfigure(0, weight=1)
        frame.grid_rowconfigure(1, weight=10)
        
        buttons_frame = tk.Frame(frame)
        buttons_frame.grid(row=1, column=0, sticky="nsew", padx=10, pady=10)

        actions = [
            ("Dodaj użytkownika", lambda: self.show_add_user_form(frame)),
            ("Usuń użytkownika", self.delete_user),
        ]

        for idx, (label, command) in enumerate(actions):
            tk.Button(buttons_frame, text=label, font=("Arial", 16), bg="gray", fg="white",
                    width=15, height=2, command=command).pack(pady=10)

        users_frame = tk.Frame(frame)
        users_frame.grid(row=1, column=1, sticky="nsew", padx=10, pady=10)

        tk.Label(users_frame, text="Lista użytkowników", font=("Arial", 18)).pack(pady=10)

        self.users_listbox = ttk.Treeview(users_frame, columns=("Username",), show="headings")
        self.users_listbox.heading("Username", text="Nazwa użytkownika")
        self.users_listbox.pack(fill="both", expand=True)
        self.users_listbox.column("Username", anchor="center")

        self.load_users()

        self.dynamic_frame = tk.Frame(frame, bg="white", relief="sunken", bd=2, width=300)
        self.dynamic_frame.grid(row=1, column=2, sticky="nsew", padx=10, pady=10)
        self.dynamic_frame.grid_propagate(False)

        buttons_frame.grid_propagate(False)
        users_frame.grid_propagate(False)  
        
    def load_users(self):
        users = self.request_handler.get_all_users()
        if users:
            self.users_listbox.delete(*self.users_listbox.get_children())
            for user in users:
                self.users_listbox.insert("", "end", values=(user['username'],))

    def show_add_user_form(self, parent_frame):
        for widget in self.dynamic_frame.winfo_children():
            widget.destroy() 

        tk.Label(self.dynamic_frame, text="Dodaj użytkownika", font=("Arial", 18)).pack(pady=10)

        tk.Label(self.dynamic_frame, text="Nazwa użytkownika:", font=("Arial", 14)).pack(pady=5)
        username_entry = tk.Entry(self.dynamic_frame, font=("Arial", 14))
        username_entry.pack(pady=5)

        tk.Label(self.dynamic_frame, text="Hasło:", font=("Arial", 14)).pack(pady=5)
        password_entry = tk.Entry(self.dynamic_frame, show="*", font=("Arial", 14))
        password_entry.pack(pady=5)

        tk.Label(self.dynamic_frame, text="Potwierdź hasło:", font=("Arial", 14)).pack(pady=5)
        confirm_password_entry = tk.Entry(self.dynamic_frame, show="*", font=("Arial", 14))
        confirm_password_entry.pack(pady=5)

        def submit():
            username = username_entry.get().strip()
            password = password_entry.get()
            confirm_password = confirm_password_entry.get()

            if not username or not password:
                tk.Label(self.dynamic_frame, text="Wszystkie pola są wymagane!", fg="red", font=("Arial", 12)).pack(pady=5)
                return

            if password != confirm_password:
                tk.Label(self.dynamic_frame, text="Hasła nie pasują!", fg="red", font=("Arial", 12)).pack(pady=5)
                return

            success = self.request_handler.create_user({"username": username, "password": password})
            if success:
                self.load_users()
                tk.Label(self.dynamic_frame, text="Użytkownik dodany!", fg="green", font=("Arial", 12)).pack(pady=5)
            else:
                tk.Label(self.dynamic_frame, text="Nie udało się dodać użytkownika.", fg="red", font=("Arial", 12)).pack(pady=5)

        tk.Button(self.dynamic_frame, text="Dodaj", font=("Arial", 14), bg="green", fg="white", command=submit).pack(pady=10)

    def delete_user(self):
        selected_item = self.users_listbox.selection()
        if selected_item:
            user_to_delete = self.users_listbox.item(selected_item, "values")[0]
            if messagebox.askyesno("Potwierdzenie", f"Czy na pewno chcesz usunąć użytkownika {user_to_delete}?"):
                success = self.request_handler.delete_user_by_username(user_to_delete)
                if success:
                    self.load_users()
                    messagebox.showinfo("Sukces", f"Usunięto użytkownika {user_to_delete}.")
                else:
                    messagebox.showerror("Błąd", "Nie udało się usunąć użytkownika.")
