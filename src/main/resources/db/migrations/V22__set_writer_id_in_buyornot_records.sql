UPDATE buyornot_votes bv
JOIN buyornots b on bv.post_id = b.id
SET bv.writer_id = b.writer_id
WHERE bv.writer_id is null;
