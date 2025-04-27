-- Insert Locations (Reference Data)
INSERT INTO Location (id, unLocode, name) VALUES (1, 'CNHKG', 'Hongkong');
INSERT INTO Location (id, unLocode, name) VALUES (2, 'AUMEL', 'Melbourne');
INSERT INTO Location (id, unLocode, name) VALUES (3, 'SESTO', 'Stockholm');
INSERT INTO Location (id, unLocode, name) VALUES (4, 'FIHEL', 'Helsinki');
INSERT INTO Location (id, unLocode, name) VALUES (5, 'USCHI', 'Chicago');
INSERT INTO Location (id, unLocode, name) VALUES (6, 'JNTKO', 'Tokyo');
INSERT INTO Location (id, unLocode, name) VALUES (7, 'DEHAM', 'Hamburg');
INSERT INTO Location (id, unLocode, name) VALUES (8, 'CNSHA', 'Shanghai');
INSERT INTO Location (id, unLocode, name) VALUES (9, 'NLRTM', 'Rotterdam');
INSERT INTO Location (id, unLocode, name) VALUES (10, 'SEGOT', 'Göteborg');
INSERT INTO Location (id, unLocode, name) VALUES (11, 'CNHGH', 'Hangzhou');
INSERT INTO Location (id, unLocode, name) VALUES (12, 'USNYC', 'New York');
INSERT INTO Location (id, unLocode, name) VALUES (13, 'USDAL', 'Dallas');

---- Insert Voyages
--INSERT INTO Voyage (id, voyage_number) VALUES (1, '0100S');
--INSERT INTO Voyage (id, voyage_number) VALUES (2, '0200T');
--INSERT INTO Voyage (id, voyage_number) VALUES (3, '0300A');
--INSERT INTO Voyage (id, voyage_number) VALUES (4, '0301S');
--INSERT INTO Voyage (id, voyage_number) VALUES (5, '0400S');
--
---- Insert CarrierMovements for voyage 0100S (HONGKONG_TO_NEW_YORK)
--INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id)
--VALUES (1, 1, 11, '2008-10-01 12:00:00', '2008-10-03 14:30:00', 1);
--INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id)
--VALUES (2, 11, 6, '2008-10-03 21:00:00', '2008-10-06 06:15:00', 1);
--INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id)
--VALUES (3, 6, 2, '2008-10-06 11:00:00', '2008-10-12 11:30:00', 1);
--INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id)
--VALUES (4, 2, 12, '2008-10-14 12:00:00', '2008-10-23 23:10:00', 1);
--
---- Insert CarrierMovements for voyage 0200T (NEW_YORK_TO_DALLAS)
--INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id)
--VALUES (5, 12, 5, '2008-10-24 07:00:00', '2008-10-24 17:45:00', 2);
--INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id)
--VALUES (6, 5, 13, '2008-10-24 21:25:00', '2008-10-25 19:30:00', 2);
--
---- Insert CarrierMovements for voyage 0300A (DALLAS_TO_HELSINKI)
--INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id)
--VALUES (7, 13, 7, '2008-10-29 03:30:00', '2008-10-31 14:00:00', 3);
--INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id)
--VALUES (8, 7, 3, '2008-11-01 15:20:00', '2008-11-01 18:40:00', 3);
--INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id)
--VALUES (9, 3, 4, '2008-11-02 09:00:00', '2008-11-02 11:15:00', 3);
--
---- Insert CarrierMovements for voyage 0400S (HELSINKI_TO_HONGKONG)
--INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id)
--VALUES (10, 4, 9, '2008-11-04 05:50:00', '2008-11-06 14:10:00', 5);
--INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id)
--VALUES (11, 9, 8, '2008-11-10 21:45:00', '2008-11-22 16:40:00', 5);
--INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id)
--VALUES (12, 8, 1, '2008-11-24 07:00:00', '2008-11-28 13:37:00', 5);
--
---- Insert Cargo ABC123
--INSERT INTO Cargo (id, tracking_id, origin_id, spec_origin_id, spec_destination_id, spec_arrival_deadline, transport_status, routing_status)
--VALUES (1, 'ABC123', 1, 1, 4, '2009-03-15 00:00:00', 'IN_PORT', 'ROUTED');
--
---- Insert Legs for ABC123
--INSERT INTO Leg (id, voyage_id, load_location_id, unload_location_id, load_time, unload_time, cargo_id)
--VALUES (1, 1, 1, 12, '2009-03-02 00:00:00', '2009-03-05 00:00:00', 1);
--INSERT INTO Leg (id, voyage_id, load_location_id, unload_location_id, load_time, unload_time, cargo_id)
--VALUES (2, 2, 12, 13, '2009-03-06 00:00:00', '2009-03-08 00:00:00', 1);
--INSERT INTO Leg (id, voyage_id, load_location_id, unload_location_id, load_time, unload_time, cargo_id)
--VALUES (3, 3, 13, 4, '2009-03-09 00:00:00', '2009-03-12 00:00:00', 1);
--
---- Insert HandlingEvents for ABC123
--INSERT INTO HandlingEvent (id, cargo_id, type, completion_time, registration_time, location_id, voyage_id)
--VALUES (1, 1, 'RECEIVE', '2009-03-01 00:00:00', '2009-03-01 00:00:00', 1, NULL);
--INSERT INTO HandlingEvent (id, cargo_id, type, completion_time, registration_time, location_id, voyage_id)
--VALUES (2, 1, 'LOAD', '2009-03-02 00:00:00', '2009-03-02 00:00:00', 1, 1);
--INSERT INTO HandlingEvent (id, cargo_id, type, completion_time, registration_time, location_id, voyage_id)
--VALUES (3, 1, 'UNLOAD', '2009-03-05 00:00:00', '2009-03-05 00:00:00', 12, 1);
--
---- Insert Cargo JKL567
--INSERT INTO Cargo (id, tracking_id, origin_id, spec_origin_id, spec_destination_id, spec_arrival_deadline, transport_status, routing_status)
--VALUES (2, 'JKL567', 11, 11, 3, '2009-03-18 00:00:00', 'IN_PORT', 'ROUTED');
--
---- Insert Legs for JKL567
--INSERT INTO Leg (id, voyage_id, load_location_id, unload_location_id, load_time, unload_time, cargo_id)
--VALUES (4, 1, 11, 12, '2009-03-03 00:00:00', '2009-03-05 00:00:00', 2);
--INSERT INTO Leg (id, voyage_id, load_location_id, unload_location_id, load_time, unload_time, cargo_id)
--VALUES (5, 2, 12, 13, '2009-03-06 00:00:00', '2009-03-08 00:00:00', 2);
--INSERT INTO Leg (id, voyage_id, load_location_id, unload_location_id, load_time, unload_time, cargo_id)
--VALUES (6, 3, 13, 3, '2009-03-09 00:00:00', '2009-03-11 00:00:00', 2);
--
---- Insert HandlingEvents for JKL567
--INSERT INTO HandlingEvent (id, cargo_id, type, completion_time, registration_time, location_id, voyage_id)
--VALUES (4, 2, 'RECEIVE', '2009-03-01 00:00:00', '2009-03-01 00:00:00', 11, NULL);
--INSERT INTO HandlingEvent (id, cargo_id, type, completion_time, registration_time, location_id, voyage_id)
--VALUES (5, 2, 'LOAD', '2009-03-03 00:00:00', '2009-03-03 00:00:00', 11, 1);
--INSERT INTO HandlingEvent (id, cargo_id, type, completion_time, registration_time, location_id, voyage_id)
--VALUES (6, 2, 'UNLOAD', '2009-03-05 00:00:00', '2009-03-05 00:00:00', 12, 1);
--INSERT INTO HandlingEvent (id, cargo_id, type, completion_time, registration_time, location_id, voyage_id)
--VALUES (7, 2, 'LOAD', '2009-03-06 00:00:00', '2009-03-06 00:00:00', 12, 1);