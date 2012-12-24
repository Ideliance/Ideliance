DROP VIEW IF EXISTS ideliance_view_subject;

DROP VIEW IF EXISTS ideliance_view_relation;

DROP VIEW IF EXISTS ideliance_view_triplet;


CREATE VIEW ideliance_view_subject AS
	SELECT s.id, d1.lang, d1.value AS entitled, d2.value AS freetext, s.datecreation, s.datemodification, s.authorcreation, s.authormodification, s.issystem
	FROM `ideliance_subject` AS s LEFT JOIN `ideliance_dictionary` AS d2 ON (s.id=d2.fk AND d2.type=2), `ideliance_dictionary` AS d1
	WHERE s.id=d1.fk AND d1.type=1 AND (d2.lang IS NULL OR d1.lang=d2.lang);


CREATE VIEW ideliance_view_relation AS
	SELECT r.id, d1.lang, d1.value AS entitled, r.inverse, d3.value AS inverseentitled, r.datecreation, r.datemodification, r.authorcreation, r.authormodification, r.issystem
	FROM `ideliance_relation` AS r LEFT JOIN `ideliance_relation` AS ri ON (r.inverse=ri.id) LEFT JOIN `ideliance_dictionary` AS d3 ON (d3.type=3 AND ri.id=d3.fk), `ideliance_dictionary` AS d1
	WHERE r.id=d1.fk AND d1.type=3 AND (d3.lang IS NULL OR d3.lang=d1.lang);


CREATE VIEW ideliance_view_triplet AS
	SELECT tp.id, ds.lang, tp.subject, ds.value AS subjectentitled, tp.relation, dr.value AS relationentitled, tp.complement, dc.value AS complemententitled, tp.datecreation, tp.authorcreation, tp.datemodification, tp.authormodification, tp.issystem
	FROM ideliance_triplet AS tp, ideliance_dictionary AS ds, ideliance_dictionary AS dr, ideliance_dictionary AS dc
	WHERE tp.typesubject=0 AND tp.typecomplement=0 AND ds.type=1 AND dr.type=3 AND dc.type=1 AND tp.subject=ds.fk AND tp.relation=dr.fk AND tp.complement=dc.fk AND ds.lang=dr.lang AND dr.lang=dc.lang;


CREATE VIEW ideliance_view_user AS
	SELECT tp.id, tp.subject, ds.value AS subjectentitled, tp.relation, dr.value AS relationentitled, tp.complement, dc.value AS complemententitled, tp.datecreation, tp.authorcreation, tp.datemodification, tp.authormodification
	FROM ideliance_triplet AS tp, ideliance_dictionary AS ds, ideliance_dictionary AS dr, ideliance_dictionary AS dc
	WHERE dr.value IN ('PASSWORD', 'LEVEL') AND ds.type=1 AND dr.type=3 AND dc.type=1 AND tp.subject=ds.fk AND tp.relation=dr.fk AND tp.complement=dc.fk AND ds.lang=dr.lang AND dr.lang=dc.lang;