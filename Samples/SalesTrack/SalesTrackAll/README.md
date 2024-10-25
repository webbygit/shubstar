Drop Table __migrationhistory;
Drop Table userclaims;
Drop Table userlogins;
Drop Table userroles;
Drop Table roles;
Drop Table users;
Drop Table userprofiles;
Drop Table businesses;
Drop Table products;
Drop Table productcategories;
Alter Table companylicenses Drop Foreign Key FK_CompanyLicenses_Companies_Company_CompanyId;
Alter Table companylicenses Drop Foreign Key FK_CompanyLicenses_Licenses_License_LicenseId;
Drop Table licenses;

Drop Table companies;
Drop Table companylicenses;
