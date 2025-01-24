INSERT INTO user (username, enabled, login_pin, user_type) VALUES ('admin',true,'12345678','ADMIN');
INSERT INTO  categories (name) VALUES ('BRAK');
INSERT INTO categories (name) VALUES ('napoje');
INSERT INTO  categories (name) VALUES ('wege');
INSERT INTO  categories (name) VALUES ('pizza');
INSERT INTO meals (price, category_id, name) VALUES (22.29,4,'roma');
INSERT INTO meals (price, category_id, name) VALUES (25.29,4,'kosa');
INSERT INTO meals (price, category_id, name) VALUES (21.59,4,'carne');
INSERT INTO meals (price, category_id, name) VALUES (33.99,4,'hawai');
INSERT INTO meals (price, category_id, name) VALUES (43.29,4,'bologna');
INSERT INTO meals (price, category_id, name) VALUES (29.99,4,'gyros');
INSERT INTO meals (price, category_id, name) VALUES (9.99,2,'cola 200ml');
INSERT INTO meals (price, category_id, name) VALUES (4.99,2,'woda 200ml');
INSERT INTO meals (price, category_id, name) VALUES (6.99,2,'fanta 200ml');
INSERT INTO meals (price, category_id, name) VALUES (8.99,2,'piwo 200ml');
INSERT INTO meals (price, category_id, name) VALUES (7.99,2,'7up 200ml');
INSERT INTO meals (price, category_id, name) VALUES (7.99,3,'sałatka Cezar');
INSERT INTO meals (price, category_id, name) VALUES (8.99,3,'sałatka grecka');
INSERT INTO meals (price, category_id, name) VALUES (9.99,3,'caprese');
-- INSERT INTO tabletops (name, size, x, y) VALUES ('Lada', 10, 0, 0);
-- INSERT INTO tabletops (name, size, x, y) VALUES ('Stolik A1', 1,0 , 1);
-- INSERT INTO tabletops (name, size, x, y) VALUES ('Stolik A2', 2,0, 2);
-- INSERT INTO tabletops (name, size, x, y) VALUES ('Stolik C1', 1,0, 2);
INSERT INTO orders (closed_at, created_at, tabletop, user_id, comment) VALUES ('2023-10-01 13:00:00','2023-10-01 12:00:00',1,1,'pilne');
INSERT INTO orders (created_at, tabletop, user_id) VALUES ('2023-10-01 12:01:00',2,1);
INSERT INTO orders (created_at, tabletop, user_id) VALUES ('2023-10-01 12:07:00',3,1);
INSERT INTO order_meal (meal_id, order_id) VALUES (1,1);
INSERT INTO order_meal (meal_id, order_id) VALUES (7,1);
INSERT INTO order_meal (meal_id, order_id) VALUES (9,1);
INSERT INTO order_meal (meal_id, order_id) VALUES (4,2);
INSERT INTO order_meal (meal_id, order_id) VALUES (5,2);
INSERT INTO order_meal (meal_id, order_id) VALUES (8,2);
INSERT INTO order_meal (meal_id, order_id) VALUES (7,2);
INSERT INTO order_meal (meal_id, order_id, comment) VALUES (3,3, 'bez soli');
INSERT INTO order_meal (meal_id, order_id) VALUES (2,3);



