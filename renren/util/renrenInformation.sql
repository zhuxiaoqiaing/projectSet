#create the table
CREATE TABLE `RenRenInfo` (  
  `id` int NOT NULL,  
  `username` varchar(20), 
   `head` varchar(200),
  `sex` varchar(20), 
  `birthday`  varchar(50),  
  `address` varchar(50),  
  `lastLogin`  varchar(60),  
  PRIMARY KEY (`id`)  
) DEFAULT CHARSET=utf8; 
#count the rows in the table
select count(*) from RenRenInfo
#query the recorder according to special condition
select count(id) from RenRenInfo where id like '4691%' and(sex!="" or username!="" or birthday!="")
select * from RenRenInfo