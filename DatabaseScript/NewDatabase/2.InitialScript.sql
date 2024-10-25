
insert into userprofiles(FirstName,LastName,EmailID,CompanyCode,Address1,Address2,City,State,Zip,Country,Latitude,Longitude,ContactPhoneNumber,IsActive,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate,OrgId,Role) values( 'Rupesh', 'Jain', 'superadmin@salestrackmobile.com', 'SALE', 'SalesTrackMobile', NULL, NULL, NULL, NULL, 'USA', NULL, NULL, NULL, '1', NULL, NULL, NULL, NULL, '10000001', 'SuperAdmin');
insert into salestrackdb.users values((SELECT uuid()),'superadmin@salestrackmobile.com',true,'AOlX77JeHqA/U+f6OnaqywSZ1JHii2qWhiPfmqc00cr6JfWpxtlCgvMh0oFoSK6S+A==',(SELECT uuid()),'1234567890',false,false,null,false,false,'superadmin@salestrackmobile.com',null,null,0,0,'ApplicationUser',1);


INSERT INTO `salestrackdb`.`roles`
(`Id`,
`Name`)
VALUES
('1c68556a-f247-4fd5-ae36-191d94eba409',
'Admin');
INSERT INTO `salestrackdb`.`roles`
(`Id`,
`Name`)
VALUES
('10ddb008-4b2d-4103-8527-88dad51bd1f9',
'Business');
INSERT INTO `salestrackdb`.`roles`
(`Id`,
`Name`)
VALUES
('44a631b5-5f93-4ce8-b27c-8e6575f534a7',
'Customer');
INSERT INTO `salestrackdb`.`roles`
(`Id`,
`Name`)
VALUES
('58b7d77b-ee87-4a5c-b368-d1b62d7b2fda',
'DataMinor');
INSERT INTO `salestrackdb`.`roles`
(`Id`,
`Name`)
VALUES
('b40199a1-68bb-427b-9e01-a703db70ffd8',
'Salesman');
INSERT INTO `salestrackdb`.`roles`
(`Id`,
`Name`)
VALUES
('17680276-3e26-4a1d-9e8b-4de05702b6cd',
'SalesManager');
INSERT INTO `salestrackdb`.`roles`
(`Id`,
`Name`)
VALUES
('f0501201-1872-4be0-9e5c-6e98d709ac3e',
'SubAdmin');
INSERT INTO `salestrackdb`.`roles`
(`Id`,
`Name`)
VALUES
('2e538e3c-62cb-4b06-b345-2349bc6921af',
'SuperAdmin');
INSERT INTO `salestrackdb`.`roles`
(`Id`,
`Name`)
VALUES
('4b59d19b-e843-44af-acd8-35da1b6ca2a9','Support')


INSERT INTO `salestrackdb`.`userroles`
(`UserId`,
`RoleId`,
`IdentityUser_Id`)
VALUES
((select userid from salestrackdb.users where email='superadmin@salestrackmobile.com'),
(select Id from salestrackdb.roles where name='SuperAdmin' ),
null);