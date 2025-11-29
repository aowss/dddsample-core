-- Insert Locations (Reference Data)
DELETE FROM LOCATION;
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (1, 'CNHKG', 'Hongkong');
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (2, 'AUMEL', 'Melbourne');
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (3, 'SESTO', 'Stockholm');
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (4, 'FIHEL', 'Helsinki');
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (5, 'USCHI', 'Chicago');
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (6, 'JNTKO', 'Tokyo');
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (7, 'DEHAM', 'Hamburg');
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (8, 'CNSHA', 'Shanghai');
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (9, 'NLRTM', 'Rotterdam');
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (10, 'SEGOT', 'Göteborg');
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (11, 'CNHGH', 'Hangzhou');
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (12, 'USNYC', 'New York');
INSERT INTO Location (id, unLocode, name) OVERRIDING SYSTEM VALUE VALUES (13, 'USDAL', 'Dallas');
COMMIT;
