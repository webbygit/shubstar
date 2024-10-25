//
//  DealsViewController.swift
//  SalesTrack
//
//  Created by Manish Behl on 04/10/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import UIKit
import MBProgressHUD
import Alamofire

class DealsViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    var dealsArray = NSArray()
    var accessToken = UserDefaults.standard.string(forKey: "accessToken")

    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.delegate = self
        tableView.dataSource = self
        self.dealsApi()
    }
    func dealsApi(){
        
        let Auth_header  = ["Authorization" :"Bearer" + " " + accessToken! ,"Content-Type": "application/json"]

        let email = UserDefaults.standard.value(forKey: "email") as? String
        let url = StructConstants.BaseURL.dealsGetUrl
        
        Alamofire.request(url, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Auth_header).responseJSON { (response) in
            
            guard response.result.error == nil,let _ = response.result.value else{
                
                CommanClass.showAlertMessage(self,titleStr:"Alert", messageStr:"Server Error")
                
                CommanClass.dismissGlobalHUD(self.view)
                return
            }
            let jsonResponce = response.result.value!
            print(jsonResponce)
            
            self.dealsArray = jsonResponce as! NSArray
            print(self.dealsArray)
            self.tableView.reloadData()
        }
    }
    @IBAction func bckBtnClicked(_ sender: UIButton) {
      self.navigationController?.popViewController(animated: true)
    }

}



extension DealsViewController : UITableViewDataSource,UITableViewDelegate {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dealsArray.count
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 1.0
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 1.0
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 160.0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell:DealsTableViewCell = self.tableView.dequeueReusableCell(withIdentifier: "cell") as! DealsTableViewCell
    
        for i in dealsArray {
            let off = (i as AnyObject).value(forKey: "Amount") as? String

            cell.discountLbl.text =  off! + "% off-"
            let pecentage = (i as AnyObject).value(forKey: "DealApplicableAs") as? String
            cell.applicationPercentageLbl.text = "applicable on" + " " + pecentage!
            cell.validDateLbl.text = (i as AnyObject).value(forKey: "StartDate") as? String
            cell.endDateLbl.text = (i as AnyObject).value(forKey: "EndDate") as? String
            
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("You tapped cell number \(indexPath.row).")
    }
}
