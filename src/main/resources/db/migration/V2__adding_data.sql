INSERT INTO product (product_name, product_type, material, manufacturer, description, price)
VALUES
('Кеды №1', 'Кеды', 'Кожа', 'Luma', 'Описание балдёжных кед', 5399),
('Кроссовки №1', 'Кроссовки', 'Кожа', 'Luma', 'Кроссовки для самых требовательных', 3999),
('Кроссовки №2', 'Кроссовки', 'Замша', 'Mike', 'Зимние утеплённые кроссовки', 7878),
('Ботинки', 'Кожаные ботинки', 'Кожа', 'ELCO', 'Кожаная кожанность', 3299),
('Пуховик уровня А', 'Пуховик', 'Полиэстер', 'Devi''s', 'Пуховик из стеганного текстиля', 6669),
('Кожанка из прошлого', 'Куртка', 'Кожа', 'Kango', 'Кожаная куртка с некогда популярным кроем', 2999),
('Пальто с капюшоном', 'Пальто', 'Шерсть', 'Kango', 'Шерстяное пальто с капюшоном', 9999),
('Футболка №1', 'Футболка', 'Хлопок', 'Lowman', 'Кто-нибудь это вообще читает?', 759),
('Футболка №2', 'Футболка', 'Хлопок', 'Luma', 'Футболка с принтом Большой волны в Канагаве', 1149),
('Футболка белая', 'Футболка', 'Хлопок', 'Kango', 'Обычная белая футболка', 700),
('Зонт складной', 'Акссесуар', 'Полиэстер', 'Kango', 'Надёжный складной зонт; любому тирану будет под стать', 499),
('Галстук', 'Акссесуар', 'Хлопок', 'Lenderson', 'Синий галстук с узорами-птицами', 2295);

INSERT INTO stock (product_id, size, quantity)
VALUES
(SELECT id FROM product WHERE product_name = 'Кеды №1', '43RU', 10),
(SELECT id FROM product WHERE product_name = 'Кеды №1', '44RU', 7),
(SELECT id FROM product WHERE product_name = 'Кеды №1', '45RU', 0),
(SELECT id FROM product WHERE product_name = 'Кроссовки №1', '37RU', 5),
(SELECT id FROM product WHERE product_name = 'Кроссовки №1', '33RU', 1),
(SELECT id FROM product WHERE product_name = 'Кроссовки №1', '35RU', 4),
(SELECT id FROM product WHERE product_name = 'Кроссовки №2', '40RU', 23),
(SELECT id FROM product WHERE product_name = 'Кроссовки №2', '41RU', 16),
(SELECT id FROM product WHERE product_name = 'Ботинки', '43RU', 30),
(SELECT id FROM product WHERE product_name = 'Пуховик уровня А', '50/52', 3),
(SELECT id FROM product WHERE product_name = 'Пуховик уровня А', '52/54', 8),
(SELECT id FROM product WHERE product_name = 'Кожанка из прошлого', '52/54', 2),
(SELECT id FROM product WHERE product_name = 'Кожанка из прошлого', '48/50', 0),
(SELECT id FROM product WHERE product_name = 'Пальто с капюшоном', '48/50', 4),
(SELECT id FROM product WHERE product_name = 'Пальто с капюшоном', '50/52', 8),
(SELECT id FROM product WHERE product_name = 'Футболка №1', 'M', 42),
(SELECT id FROM product WHERE product_name = 'Футболка №1', 'L', 37),
(SELECT id FROM product WHERE product_name = 'Футболка №2', 'S', 51),
(SELECT id FROM product WHERE product_name = 'Футболка №2', 'M', 39),
(SELECT id FROM product WHERE product_name = 'Футболка белая', 'L', 0),
(SELECT id FROM product WHERE product_name = 'Футболка белая', 'M', 15),
(SELECT id FROM product WHERE product_name = 'Зонт складной', '-', 13),
(SELECT id FROM product WHERE product_name = 'Галстук', '-', 10);


INSERT INTO customer (first_name, last_name, login, email)
VALUES
('Иванов', 'Иван', 'ivanov', 'ivanov@jandex.ru'),
('Петров', 'Пётр', 'petrov', 'sudzumi@jandex.ru');

INSERT INTO orders (customer_id, ordered_time, delivered)
VALUES
(1, PARSEDATETIME('04.05.2020 17:32:00', 'dd.MM.yyyy hh:mm:ss'), TRUE),
(1, PARSEDATETIME('05.05.2020 08:58:00', 'dd.MM.yyyy hh:mm:ss'), FALSE);

INSERT INTO orders_product (order_id, product_id)
VALUES
(1, 1),
(1, 6),
(1, 3),
(1, 9),
(1, 9),
(1, 10),
(2, 1),
(2, 1);