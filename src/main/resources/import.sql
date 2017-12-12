
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (1,'generic','statuses','Status','Status',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (2,'generic','ingredients','Ingredient','Ingredient',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (3,'generic','products','Product','Product',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (4,'generic','licenses','License','License',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (5,'generic','socs','SOC','SOC',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (6,'generic','hlgts','HLGT','HLGT',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (7,'generic','hlts','HLT','HLT',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (8,'generic','pts','PT','PT',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (9,'generic','owners','Owner','Owner',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (10,'generic','assignees','Assigned To','Assigned To',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (11,'generic','daterange','Date Range','Date Range',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (12,'signal','signalconfirmation','Signal Confirmation','Signal Confirmation',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (13,'signal','signalsource','Signal Source','Signal Source',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (14,'assessment','assessmenttaskstatus','Assessment Task Status','Assessment Task Status',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (15,'assessment','finaldispositions','Final Disposition','Final Disposition',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (16,'risk','riskplanactionstatus','Risk Plan Action Status','Risk Plan Action Status',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (17,'detection','frequency','Frequency','Frequency',TRUE);




INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (1, 'New', 'New', 'New', TRUE,1);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (2, 'In Progress', 'In Progress', 'In Progress', TRUE,1);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (3, 'Completed', 'Completed', 'Completed', TRUE,1);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (4, 'Overdue', 'Overdue', 'Overdue', TRUE,1);


