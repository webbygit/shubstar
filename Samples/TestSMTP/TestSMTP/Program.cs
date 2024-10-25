using System.Net.Mail;
using System.Net.Mime;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;

string MessageBody = @"<div style='padding:10px;'><strong>Dear  "
+ "Rupesh"
+ ",</strong><br/><p>Thank You for Registration In Salestrack. <br/> Your Login EmailID :- "
+ " <br/> Password :- "
+ "</p><p>This is an auto-generated mail. please do not reply.</p><p>Thanks,</p></br><p><img src=\"https://salestrackadmin.azurewebsites.net/Images/EmailLogo.png" + "\" alt=\"SalesTrack\" style=\"width:180px;height:50px;\"></p></div>";

var sendGridUserName = "SalesTrack";
//var sentFrom = "salestrackmobileapp@gmail.com";
//var sentFrom = "info@salestrackmobileapp.com";
var sentFrom = "no-reply@salestrackmobile.com";
var userName = "tesgtr7@gmail.com";
////var sendGridPassword = "Pass@123";
var AppPassword = "qiib thii rtgv opoj";

var client = new System.Net.Mail.SmtpClient("smtp.gmail.com", Convert.ToInt32(587));
//var client = new System.Net.Mail.SmtpClient("smtp.gmail.com", 587);
client.EnableSsl = true;
client.Port = 587;
client.DeliveryMethod = System.Net.Mail.SmtpDeliveryMethod.Network;
client.UseDefaultCredentials = false;

//System.Net.NetworkCredential credentials = new System.Net.NetworkCredential(sendGridUserName, sendGridPassword);
System.Net.NetworkCredential credentials = new System.Net.NetworkCredential();
credentials.UserName = userName;
//credentials.Password = sendGridPassword;
credentials.Password = AppPassword;
client.Credentials = credentials;

// Create the message:
var mail = new System.Net.Mail.MailMessage();
mail.From = new MailAddress(sentFrom);
mail.To.Add(new MailAddress("jainrupesh@gmail.com"));
mail.Subject = "Test message from SalesTrackMobile Test Application";
mail.AlternateViews.Add(AlternateView.CreateAlternateViewFromString(MessageBody, null, MediaTypeNames.Text.Plain));
mail.AlternateViews.Add(AlternateView.CreateAlternateViewFromString(MessageBody, null, MediaTypeNames.Text.Html));
mail.Body = MessageBody;

try
{
    client.Send(mail);
    //var ret = client.SendMail(mail);
    //Console.WriteLine($"ret: {ret.Status}");
    //Console.WriteLine($"Exception: {ret.Exception}");
    //Console.WriteLine($"Completed: {ret.IsCompletedSuccessfully}");
    //Console.WriteLine($"IsFaulted: {ret.IsFaulted}");

}
catch (Exception)
{
    throw;
}




//var client = new SmtpClient("smtp.gmail.com", 587)
//{
//    //Credentials = new NetworkCredential(userName, sendGridPassword),
//    Credentials = new NetworkCredential(userName, AppPassword),
//    EnableSsl = true
//};

//client.Send(sentFrom, "jainrupesh@gmail.com", "test", "testbody");
//Console.WriteLine("Sent");
