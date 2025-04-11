INSERT INTO patient VALUES (1, 'Test', 'TestNone', '1966-12-31', 'F', '1 Brookside St', '100-222-3333')
    ON CONFLICT DO NOTHING;
INSERT INTO patient VALUES (2, 'Test', 'TestBorderline', '1945-06-24', 'M', '2 High St', '200-333-4444')
    ON CONFLICT DO NOTHING;
INSERT INTO patient VALUES (3, 'Test', 'TestInDanger', '2004-06-18', 'M', '3 Club Road', '300-444-5555')
    ON CONFLICT DO NOTHING;
INSERT INTO patient VALUES (4, 'Test', 'TestEarlyOnset', '2002-06-28', 'F', '4 Valley Dr', '400-555-6666')
    ON CONFLICT DO NOTHING;
