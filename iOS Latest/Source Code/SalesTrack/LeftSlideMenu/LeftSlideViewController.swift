//
//  LeftSlideViewController.swift
//  SalesTrack
//
//  Created by Manish Behl on 28/09/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import UIKit
import MBProgressHUD
import CoreData

class LeftSlideViewController: UIViewController,UITableViewDelegate {

    @IBOutlet weak var tableView: UITableView!
    var swiftBlogsTitles: [String]  = []
    let textCellIdentifier = "TextCell"
    var imageName: [String]  = []

    
    
    override func viewDidLoad() {
        super.viewDidLoad()
         swiftBlogsTitles = ["Poultry Health Monitoring","Start New Session","Open Existing Session","Training & Education", "Reports","Help","Settings","Species"]
        tableView.isScrollEnabled = true
        tableView.reloadData()
     imageName = ["slider_dashboard", "slider_start_new_session", "slider_open_existing","slider_training","slider_reports","slider_help","slider_setings","slider_switch"]
        
    }
    

    func numberOfSectionsInTableView(_ tableView: UITableView) -> Int {
        return 1
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 110
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return 8
    }
    
    func tableView(_ tableView: UITableView, cellForRowAtIndexPath indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: textCellIdentifier, for: indexPath)
//        let row = indexPath.row
//        for  contentview in cell.contentView.subviews {
//            if contentview.isKind(of: UILabel.self) {
//                let lab = contentview as! UILabel
//                lab.text = ""
//            }
//        }
//        let iconImage : UIImageView = cell.viewWithTag(10) as! UIImageView
        let iconName : UILabel = cell.viewWithTag(20) as! UILabel
//        let image = UIImage.init(named: swiftBlogs[row] )
//        if iconImage.image == nil {
//            imageView = UIImageView.init(image: image)
//        } else {
//            iconImage.image = image
//        }
        iconName.font = UIFont(name: "Helvetica", size: 16)
//        iconImage.image = image
       // iconName.text = swiftBlogsTitles[row]
//        imageView.frame = CGRect(x: 0, y: 0, width: 158, height: 109)
        tableView.separatorColor = UIColor.black
        tableView.frame = CGRect(x: 0, y: 0, width: self.view.frame.size.width/7, height: self.view.frame.size.height)
        cell.selectionStyle = UITableViewCellSelectionStyle.none
        return cell
    }
    
//    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
//        switch indexPath.row {
//        case 0:
//            
//            imageName[0] = "slider_dashboard"
//            imageName[1] = "slider_start_new_session"
//            imageName[2] = "slider_open_existing"
//            imageName[3] = "slider_training"
//            imageName[4] = "slider_reports"
//            imageName[5] =  "slider_help"
//            imageName[6] = "slider_setings"
//            
//               // SlideNavigationController.sharedInstance().popAllAndSwitch(to: self.storyboard?.instantiateViewController(withIdentifier: "DashView_Controller"), withCompletion: nil)
//            
//            
//        case 1:
//            imageName[0] = "slider_dashboard"
//            imageName[1] = "slider_start_new_session"
//            imageName[2] = "slider_open_existing"
//            imageName[3] = "slider_training"
//            imageName[4] = "slider_reports"
//            imageName[5] =  "slider_help"
//            imageName[6] = "slider_setings"
//                
//              
//           
//
//            
//        default:
//        
//        
//        self.tableView.reloadData()
//    }
//    


}
