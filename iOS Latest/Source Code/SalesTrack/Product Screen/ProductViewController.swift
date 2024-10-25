//
//  ProductViewController.swift
//  SalesTrack
//
//  Created by Alok Yadav on 9/28/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import UIKit
import GoogleMaps
import CoreLocation
import MBProgressHUD
import Alamofire

class ProductViewController: UIViewController {

    @IBOutlet weak var searchBar: UISearchBar!
    @IBOutlet weak var tableView: UITableView!
    var accessToken = UserDefaults.standard.string(forKey: "accessToken")
    @IBOutlet weak var productCatogryLbl: UILabel!
    var droperTableView  =  UITableView ()
    var buttonPressFlag = 0

    @IBOutlet weak var productBrandLbl: UILabel!

    let animals: [String] = ["Horse", "Cow", "Camel", "Sheep", "Goat","Horse", "Cow", "Camel", "Sheep", "Goat","Horse", "Cow", "Camel", "Sheep", "Goat"]

    var cateogoryArray: [String] = ["Mobile Phones","Oppo Electronics","Electronics"]
    var brandArray = NSArray()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.delegate = self
        tableView.dataSource = self

      
     
        self.productGetAllApi()
        self.selectBrandGetApi()
        self.productCategoryGetApi()
    }

    
    var dataArray = NSArray()
    func productGetAllApi(){
        
            let strToken = UserDefaults.standard.value(forKey: "Token") as? String
            let Auth_header  = ["Authorization" :"Bearer" + " " + accessToken! ,"Content-Type": "application/json"]
            
            let url = StructConstants.BaseURL.productUrl
            
            Alamofire.request(url, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Auth_header).responseJSON { (response) in
                
                guard response.result.error == nil,let _ = response.result.value else{
                    
                    CommanClass.showAlertMessage(self,titleStr:"Alert", messageStr:"Server Error")
                    
                    CommanClass.dismissGlobalHUD(self.view)
                    return
                }
                let jsonResponce = response.result.value
                print(jsonResponce)
                self.dataArray = jsonResponce as! NSArray
                print(self.dataArray)
                self.tableView.reloadData()
            }
        
    }
    func productCategoryGetApi(){
        
        let strToken = UserDefaults.standard.value(forKey: "Token") as? String
        let Auth_header  = ["Authorization" :"Bearer" + " " + accessToken! ,"Content-Type": "application/json"]
        
        let url = StructConstants.BaseURL.productCategoryUrl
        
        Alamofire.request(url, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Auth_header).responseJSON { (response) in
            
            guard response.result.error == nil,let _ = response.result.value else{
                
                CommanClass.showAlertMessage(self,titleStr:"Alert", messageStr:"Server Error")
                
                CommanClass.dismissGlobalHUD(self.view)
                return
            }
            let jsonResponce = response.result.value
            print(jsonResponce)
            self.dataArray = jsonResponce as! NSArray
            print(self.dataArray)
            self.tableView.reloadData()
        }
        
    }
    func selectBrandGetApi(){
        
        let strToken = UserDefaults.standard.value(forKey: "Token") as? String
        let Auth_header  = ["Authorization" :"Bearer" + " " + accessToken! ,"Content-Type": "application/json"]
        
        let url = StructConstants.BaseURL.selectBrandUrl
        
        Alamofire.request(url, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: Auth_header).responseJSON { (response) in
            
            guard response.result.error == nil,let _ = response.result.value else{
                
                CommanClass.showAlertMessage(self,titleStr:"Alert", messageStr:"Server Error")
                
                CommanClass.dismissGlobalHUD(self.view)
                return
            }
            let jsonResponce = response.result.value
            print(jsonResponce)
            self.brandArray = jsonResponce as! NSArray
            print(self.brandArray)
            
            
            self.tableView.reloadData()
        }
        
    }
    
  
    @IBAction func productCategoryBtn(_ sender: UIButton) {
        buttonPressFlag = 1
        self.tableViewpop()
        droperTableView.frame = CGRect( x: 10, y: 200, width: 190, height: 100)
        droperTableView.reloadData()
    }
    @IBAction func productBrandBtn(_ sender: UIButton) {
        buttonPressFlag = 2
        self.tableViewpop()
        droperTableView.frame = CGRect( x: 210, y: 200, width: 190, height: 100)
        droperTableView.reloadData()
    }

    @IBAction func bckBtnClicked(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func addToCartClicked(_ sender: UIButton) {
    }
    func tableViewpop(){
        
        
        buttonbg1.frame = CGRect(x: 0, y: 0, width: 945, height: 340)
        buttonbg1.backgroundColor = UIColor.clear
        buttonbg1.addTarget(self, action:#selector(ProductViewController.buttonPreddDroper), for: .touchUpInside)
        
        //   buttonbg1.backgroundColor = UIColor(red: 45/255, green:45/255, blue:45/255, alpha:0.3)
        self.view.addSubview(buttonbg1)
        
        droperTableView.delegate = self
        droperTableView.dataSource = self
        
        droperTableView.layer.borderColor =  UIColor.white.cgColor
        
        buttonbg1.addSubview(droperTableView)
        
    }
    let buttonbg1 = UIButton ()
    @objc func buttonPreddDroper() {
        
        buttonbg1.removeFromSuperview()
    }
}
extension ProductViewController : UITableViewDataSource,UITableViewDelegate {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if tableView == droperTableView {
            return cateogoryArray.count
        } else if tableView == tableView {
            
            return dataArray.count
        }else {
        return 0
        }
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 1.0
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 1.0
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if tableView == droperTableView {
            return 35.0
        } else if tableView == tableView {
            
            return 141.0
        }else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {

        
        let cell = UITableViewCell ()
        
        if tableView == droperTableView {
            
            if buttonPressFlag == 1 {
                
                cell.textLabel!.text = self.cateogoryArray[indexPath.row]
                
            } else if buttonPressFlag == 2 {
                
//                var brandN = brandArray[indexPath.row] as AnyObject).value(forKey: "BrandName") as? String
//
//
//
//
//                cell.textLabel!.text = self.brandArray[indexPath.row] as! String
                
            }
        } else  if tableView == tableView {
            
            let cell:ProductCell = self.tableView.dequeueReusableCell(withIdentifier: "cell") as! ProductCell
            cell.priceLbl.text = (dataArray[indexPath.row] as AnyObject).value(forKey: "BrandName") as? String
            let price = (dataArray[indexPath.row] as AnyObject).value(forKey: "PurchasePrice") as! NSNumber
            cell.phoneNameLbl.text = String(describing: price)
            cell.descriptionLbl?.text = (dataArray[indexPath.row] as AnyObject).value(forKey: "Description") as! String
            DispatchQueue.main.async {

            let urlImage = (self.dataArray[indexPath.row] as AnyObject).value(forKey: "ImageName") as! String
            let url = URL(string:urlImage)
            if let dataImage = try? Data(contentsOf: url!) {
                cell.imgView?.image = UIImage(data: dataImage)!
                cell.imgView.contentMode = .scaleAspectFit
            }
            }
            cell.addToCartBtn.layer.cornerRadius = 15
            
            return cell
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("You tapped cell number \(indexPath.row).")
    }
    
}
