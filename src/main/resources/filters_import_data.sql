INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (1 ,'signal','statuses','Status','Status',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (2 ,'assessment','statuses','Status','Status',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (3 ,'risk','statuses','Status','Status',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (4 ,'signal','ingredients','Ingredient','Ingredients',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (5 ,'assessment','ingredients','Ingredient','Ingredients',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (6 ,'risk','ingredients','Ingredient','Ingredients',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (7 ,'signal','smProducts','Product','Products',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (8 ,'assessment','smProducts','Product','Products',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (9 ,'risk','smProducts','Product','Products',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (10,'generic','products','Product','Product',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (11,'generic','conditions','Condition','Condition',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (12,'generic','owners','Owner','Owner',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (13,'generic','assignees','Assigned To','Assigned To',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (14,'signal','signalconfirmation','Signal Confirmation','Signal Confirmation',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (15,'signal','sourceLabel','Signal Method','Signal Method',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (16,'signal','signalsource','Signal Source','Signal Source',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (17,'assessment','assessmenttaskstatus','Assessment Task Status','Assessment Task Status',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (18,'assessment','finaldispositions','Final Disposition','Final Disposition',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (19,'risk','riskplanactionstatus','Risk Plan Action Status','Risk Plan Action Status',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (20,'detection','frequency','Frequency','Frequency',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (21,'signal','detectedDates','Detected Date','Detected Date',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (22,'assessment','detectedDates','Detected Date','Detected Date',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (23,'risk','detectedDates','Detected Date','Detected Date',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (24,'signal','dueDates','Due Date','Due Date',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (25,'assessment','dueDates','Due Date','Due Date',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (26,'risk','dueDates','Due Date','Due Date',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (27,'detection','createdDates','Created Date','Created Date',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (28,'detection','lastRunDates','Last Run Date','Last Run Date',TRUE);
INSERT INTO sm_filters(filter_id, type, key, name, description, visible) VALUES (29,'detection','nextRunDates','Next Run Date','Next Run Date',TRUE);




INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (1, 'New', 'New', 'New', TRUE,1);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (2, 'In Progress', 'In Progress', 'In Progress', TRUE,1);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (3, 'Completed', 'Completed', 'Completed', TRUE,1);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (4, 'Overdue', 'Overdue', 'Overdue', TRUE,1);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (5, 'New', 'New', 'New', TRUE,2);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (6, 'In Progress', 'In Progress', 'In Progress', TRUE,2);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (7, 'Completed', 'Completed', 'Completed', TRUE,2);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (8, 'Overdue', 'Overdue', 'Overdue', TRUE,2);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (9, 'New', 'New', 'New', TRUE,3);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (10, 'In Progress', 'In Progress', 'In Progress', TRUE,3);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (11, 'Completed', 'Completed', 'Completed', TRUE,3);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (12, 'Overdue', 'Overdue', 'Overdue', TRUE,3);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (13, 'Completed', 'Completed', 'Completed', TRUE,10);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (14, 'Not Completed', 'Not Completed', 'Not Completed', TRUE,10);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (15, 'Completed', 'Completed', 'Completed', TRUE,12);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (16, 'Not Completed', 'Not Completed', 'Not Completed', TRUE,12);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (17, 'Daily', 'Daily', 'Daily', TRUE,13);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (18, 'Weekly', 'Weekly', 'Weekly', TRUE,13);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (19, 'Monthly', 'Monthly', 'Monthly', TRUE,13);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (20, 'Quarterly', 'Quarterly', 'Quarterly', TRUE,13);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (21, 'Yearly', 'Yearly', 'Yearly', TRUE,13);	
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (22, 'Manual', 'Manual', 'Manual', TRUE,15);
INSERT INTO sm_filter_status(id, key, name, description, visible,filter_id) VALUES (23, 'Automated', 'Automated', 'Automated', TRUE,15);
   
