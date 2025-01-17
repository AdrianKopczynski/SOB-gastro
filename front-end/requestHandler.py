import requests
import json
import os

class RequestHandler:
    def __init__(self, base_url):
        self.base_url = base_url

    def send_get_request(self, endpoint, params=None):
        """
        Wysyła zapytanie GET do określonego endpointa API.
        
        :param endpoint: Ścieżka endpointa API
        :param params: Parametry zapytania (opcjonalne)
        :return: JSON 
        """
        try:
            url = f"{self.base_url}/{endpoint}"
            response = requests.get(url, params=params)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.HTTPError as http_e:
            print(f"HTTP error occurred: {http_e}")
        except Exception as e:
            print(f"Other error occurred: {e}")
        return None

    def send_post_request(self, endpoint, data=None):
        """
        Wysyła zapytanie POST do określonego endpointa API.
        
        :param endpoint: Ścieżka endpointa API
        :param data: Dane do wysłania w body zapytania (opcjonalne)
        :return: JSON
        """
        try:
            url = f"{self.base_url}/{endpoint}"
            response = requests.post(url, json=data)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.HTTPError as http_e:
            print(f"HTTP error occurred: {http_e}")
        except Exception as e:
            print(f"Other error occurred: {e}")
        return None
    
    def send_delete_request(self, endpoint, params=None):
        """
        Wysyła zapytanie DELETE do określonego endpointa API.
        
        :param endpoint: Ścieżka endpointa API
        :param params: Parametry zapytania (opcjonalne)
        :return: JSON
        """
        try:
            url = f"{self.base_url}/{endpoint}"
            response = requests.delete(url, params=params)
            response.raise_for_status()
            return response.json() if response.content else {'message': 'Deleted successfully'}
        except requests.exceptions.HTTPError as http_e:
            print(f"HTTP error occurred: {http_e}")
        except Exception as e:
            print(f"Other error occurred: {e}")
        return None
    
    #--------------------------------------------------------------#
    #----------------------------USER------------------------------#
    #--------------------------------------------------------------#

    def get_user_by_pin(self, pin):
        return self.send_get_request(f"user/getUserByPin/{pin}")

    def get_user_by_id(self, id):
        return self.send_get_request(f"user/getUserById/{id}")
    
    def get_all_users(self):
        return self.send_get_request("user/getAllUsers")

    def get_user_by_username(self, username):
        return self.send_get_request(f"user/getUserByUsername{username}")

    def create_user(self, user):
        return self.send_post_request("user/createUser", user)

    def delete_user_by_id(self, id):
        return self.send_delete_request(f"user/deleteUserById/{id}")

    #--------------------------------------------------------------#
    #---------------------------ORDER------------------------------#
    #--------------------------------------------------------------#

    def get_orders_by_table(self, table_id):
        return self.send_get_request("order/getOrdersByTable", {"Table_id": table_id})

    def create_order(self, order_data):
        return self.send_post_request("order/createOrder", order_data)

    def edit_order(self, order_id, updated_data):
        return self.send_post_request("order/updateOrder", {"order_id": order_id, **updated_data})

    def delete_order(self, order_id):
        return self.send_delete_request("order/deleteOrder", {"order_id": order_id})

    #--------------------------------------------------------------#
    #-------------------------MENAGEMENT---------------------------#
    #--------------------------------------------------------------#
    
    def create_category(self, name):
        return self.send_post_request("management/addCategory", name)

    def get_category(self):
        return self.send_get_request("management/getCategoryList")

    def add_meal(self,meal):
        return self.send_post_request("management/addMeal", meal)

    def add_tabletop(self):
        return self.send_post_request("management/addTabletop")

    def get_meals(self):
        return self.send_get_request("management/getMealList")


