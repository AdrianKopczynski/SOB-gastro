INSERT INTO user (username, enabled, login_pin, user_type) VALUES ('admin',true,'12345678','ADMIN');
INSERT INTO  categories (name) VALUES ('pizza');
INSERT INTO categories (name) VALUES ('napoje');
INSERT INTO  categories (name) VALUES ('wege');
INSERT INTO meals (price, category_id, name) VALUES (22.29,1,'roma');
INSERT INTO meals (price, category_id, name) VALUES (25.29,1,'kosa');
INSERT INTO meals (price, category_id, name) VALUES (21.59,1,'carne');
INSERT INTO meals (price, category_id, name) VALUES (33.99,1,'hawai');
INSERT INTO meals (price, category_id, name) VALUES (43.29,1,'bologna');
INSERT INTO meals (price, category_id, name) VALUES (29.99,1,'gyros');
INSERT INTO meals (price, category_id, name) VALUES (9.99,2,'cola 200ml');
INSERT INTO meals (price, category_id, name) VALUES (4.99,2,'woda 200ml');
INSERT INTO meals (price, category_id, name) VALUES (6.99,2,'fanta 200ml');
INSERT INTO meals (price, category_id, name) VALUES (8.99,2,'piwo 200ml');
INSERT INTO meals (price, category_id, name) VALUES (7.99,2,'7up 200ml');
INSERT INTO meals (price, category_id, name) VALUES (7.99,3,'sałatka Cezar');
INSERT INTO meals (price, category_id, name) VALUES (8.99,3,'sałatka grecka');
INSERT INTO meals (price, category_id, name) VALUES (9.99,3,'caprese');
INSERT INTO tabletops (is_available, name) VALUES (true,'Lada');
INSERT INTO tabletops (is_available, name) VALUES (true,'Stolik A1');
INSERT INTO tabletops (is_available, name) VALUES (true,'Stolik A2');
INSERT INTO tabletops (is_available, name) VALUES (true,'Stolik C1');



