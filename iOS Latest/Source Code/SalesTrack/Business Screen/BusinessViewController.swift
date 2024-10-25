//
//  BusinessViewController.swift
//  SalesTrack
//
//  Created by Manish Behl on 27/09/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import UIKit
import CoreLocation
import MBProgressHUD
import Alamofire

class BusinessViewController: UIViewController {

  //  let animals: [String] = ["Horse", "Cow", "Camel", "Sheep", "Goat","Horse", "Cow", "Camel", "Sheep", "Goat","Horse", "Cow", "Camel", "Sheep", "Goat"]
    let statesArray: [String] = ["rtk", "del", "pani", "subhga","rtk", "del", "pani", "subhga","rtk", "del", "pani", "subhga","rtk", "del", "pani", "subhga"]
    let cityArray: [String] = ["Horse", "Cow", "Camel", "Sheep", "Goat","Horse", "Cow", "Camel", "Sheep", "Goat","Horse", "Cow", "Camel", "Sheep", "Goat"]

    var buttonPressFlag = 0
    @IBOutlet weak var statesBtnOutlet: UIButton!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var searchBar: UISearchBar!
    var droperTableView = UITableView ()
    let buttonbg1 = UIButton ()
    var msgArray = NSArray()
    @IBOutlet weak var cityLabel: UILabel!
    @IBOutlet weak var stateLabel: UILabel!
    @IBOutlet weak var cityBtnOutlet: UIButton!

    var accessToken = UserDefaults.standard.string(forKey: "accessToken")

    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.delegate = self
        tableView.dataSource = self
        self.bussinesApi()
    }
    override func viewWillAppear(_ animated: Bool) {
        

    }
    func bussinesApi(){
        
        let Auth_header  = ["Authorization" :"Bearer" + " " + accessToken! ,"Content-Type": "application/json"]

        let email = UserDefaults.standard.value(forKey: "email") as? String
        let url = StructConstants.BaseURL.businessUrl
        
        Alamofire.request(url, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Auth_header).responseJSON { (response) in
            
            guard response.result.error == nil,let _ = response.result.value else{
                
                CommanClass.showAlertMessage(self,titleStr:"Alert", messageStr:"Server Error")

                CommanClass.dismissGlobalHUD(self.view)
                return
            }
            let jsonResponce = response.result.value
            print(jsonResponce ?? "")
            self.msgArray = jsonResponce as! NSArray
            print(self.msgArray)
            self.tableView.reloadData()
        }
    }
    
    @IBAction func bckBtnClicked(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func addToCartClicked(_ sender: UIButton) {
    }

    @IBAction func statesBtnClicked(_ sender: UIButton) {
        buttonPressFlag = 1
        self.tableViewpop()
        droperTableView.frame = CGRect( x: 10, y: 200, width: 190, height: 100)
        droperTableView.reloadData()
    }
    
    @IBAction func cityBtnClicked(_ sender: Any) {
        buttonPressFlag = 2
        self.tableViewpop()
        droperTableView.frame = CGRect( x: 210, y: 200, width: 190, height: 100)
        droperTableView.reloadData()
    }
    
    func tableViewpop(){
        
        buttonbg1.frame = CGRect(x: 0, y: 0, width: 945, height: 340)
        buttonbg1.backgroundColor = UIColor.clear
        buttonbg1.addTarget(self, action:#selector(BusinessViewController.buttonPreddDroper), for: .touchUpInside)
        self.view.addSubview(buttonbg1)
        droperTableView.delegate = self
        droperTableView.dataSource = self
        droperTableView.layer.borderColor =  UIColor.white.cgColor
        buttonbg1.addSubview(droperTableView)
    }

    @objc func buttonPreddDroper() {
        
        buttonbg1.removeFromSuperview()
    }
}
extension BusinessViewController : UITableViewDelegate,UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if tableView == droperTableView {
            return statesArray.count
        } else if tableView == tableView {
            
            return msgArray.count
        }else {
        return 0
    }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = UITableViewCell ()

        if tableView == droperTableView {

            if buttonPressFlag == 1 {
            
                cell.textLabel!.text = self.statesArray[indexPath.row]
                
            } else if buttonPressFlag == 2 {
                
                cell.textLabel!.text = self.cityArray[indexPath.row]
                
            }
        } else  if tableView == tableView {
            let cell:BusinessTableViewCell = tableView.dequeueReusableCell(withIdentifier: "cell") as! BusinessTableViewCell

            for i in self.msgArray {
                cell.addressLbl.text = (i as AnyObject).value(forKey: "Address1") as? String
                cell.companyNameLbl.text = (i as AnyObject).value(forKey: "BusnessName") as? String
                 cell.cityLbl.text = (i as AnyObject).value(forKey: "City") as? String
                let urlImage  = (i as AnyObject).value(forKey: "ImageName") as? String
                let url = URL(string:urlImage!)
                if let dataImage = try? Data(contentsOf: url!) {
                   cell.imgView?.image = UIImage(data: dataImage)!
                }
            }
            return cell

        }
        return cell

    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if tableView == droperTableView {
            
            if buttonPressFlag == 1 {
                
                let statesArray = self.statesArray[indexPath.row]
                stateLabel.text = statesArray
                
            } else if buttonPressFlag == 2 {
                
                cityBtnOutlet.titleLabel?.text = self.cityArray[indexPath.row]
            
            }
            buttonPreddDroper()
        }
    }
}
