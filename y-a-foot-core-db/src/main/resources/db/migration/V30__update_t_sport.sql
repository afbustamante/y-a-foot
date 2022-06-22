UPDATE t_sport SET spt_code = 'CRICKET', spt_name = 'Cricket' WHERE spt_id = 9;

UPDATE t_sport SET spt_rank = 1 WHERE spt_code = 'FOOTBALL';
UPDATE t_sport SET spt_rank = 2 WHERE spt_code = 'CRICKET';
UPDATE t_sport SET spt_rank = 3 WHERE spt_code = 'BASKETBALL';
UPDATE t_sport SET spt_rank = 4 WHERE spt_code = 'HOCKEY';
UPDATE t_sport SET spt_rank = 5 WHERE spt_code = 'TENNIS';
UPDATE t_sport SET spt_rank = 6 WHERE spt_code = 'VOLLEYBALL';
UPDATE t_sport SET spt_rank = 7 WHERE spt_code = 'TABLE_TENNIS';
UPDATE t_sport SET spt_rank = 8 WHERE spt_code = 'BASEBALL';
UPDATE t_sport SET spt_rank = 9 WHERE spt_code = 'RUGBY';
UPDATE t_sport SET spt_rank = 10 WHERE spt_code = 'HANDBALL';
UPDATE t_sport SET spt_rank = 11 WHERE spt_code = 'BADMINTON';
UPDATE t_sport SET spt_rank = 12 WHERE spt_code = 'FUTSAL';
UPDATE t_sport SET spt_rank = 13 WHERE spt_code = 'ICE_HOCKEY';
UPDATE t_sport SET spt_rank = 14 WHERE spt_code = 'SWIMMING';
UPDATE t_sport SET spt_rank = 99 WHERE spt_code = 'OTHER';

COMMIT;