//
//  OrdersViewController.swift
//  SalesTrack
//
//  Created by Manish Behl on 27/09/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import UIKit
import MBProgressHUD
import Alamofire

class OrdersViewController: UIViewController {

    @IBOutlet weak var orderHistoryLbl: UILabel!
    var accessToken = UserDefaults.standard.string(forKey: "accessToken")
    @IBOutlet weak var tableView: UITableView!
    var orderArray = NSArray()
    var dict = [String: Any]()

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        tableView.delegate = self
        tableView.dataSource = self
        self.orderApi()
    }
    
    func orderApi(){
        
        let Auth_header  = ["Authorization" :"Bearer" + " " + accessToken! ,"Content-Type": "application/json"]
        let email = UserDefaults.standard.value(forKey: "email") as? String
        let url = StructConstants.BaseURL.orderGetUrl
        
        
        
        
        Alamofire.request(url, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Auth_header).responseString { (response) in
            
            guard response.result.error == nil,let _ = response.result.value else{
                
                CommanClass.showAlertMessage(self,titleStr:"Alert", messageStr:"Server Error")
                
                CommanClass.dismissGlobalHUD(self.view)
                return
            }
            let jsonResponce = response.result.value! as String
            print(jsonResponce)
            self.orderArray = (CommanClass.convertToArray(text: jsonResponce))
             print(self.orderArray)
           // self.orderArray = jsonResponce
            self.tableView.reloadData()
        }
    }

    @IBAction func addToCartClicked(_ sender: Any) {
    }
    
    @IBAction func bckBtnClicked(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
}

extension OrdersViewController : UITableViewDelegate,UITableViewDataSource {


func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
    return self.orderArray.count
}

// create a cell for each table view row
func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
    // create a new cell if needed or reuse an old one
    let cell:OrdersTableViewCell = self.tableView.dequeueReusableCell(withIdentifier: "cell") as! OrdersTableViewCell!
    

    for i in orderArray {
        let BusinessArray = (i as AnyObject).value(forKey: "Business") as! NSDictionary

        let typeBusiness = BusinessArray.value(forKey: "BusinessType") as? String

        let businessName = (i as AnyObject).value(forKey: "BusinessName") as? String
        print(businessName!)
        let orderNumber = (i as AnyObject).value(forKey: "OrderNo") as? String
        let totalOrderValue = (i as AnyObject).value(forKey: "TotalOrderValue") as? NSNumber
        let orderDate = (i as AnyObject).value(forKey: "OrderDate") as? String

        cell.businessName.text = "Business:" + businessName!
        cell.businessType.text = "Business Type:" + typeBusiness!
        cell.orderNumber.text = "Order Number:" + orderNumber!
        cell.totalAccount.text = "Total Amount:" + String(describing:totalOrderValue!)
        cell.orderOn.text = "Order on:" + orderDate!

    }
    
    return cell
}

// method to run when table view cell is tapped
func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
    print("You tapped cell number \(indexPath.row).")
}
}
