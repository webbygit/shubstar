//
//  CommanClass.swift
//  SalesTrack
//
//  Created by manish on 30/09/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import Foundation
import MBProgressHUD

class CommanClass{
    

    static func showAlertMessage(_ vc: UIViewController, titleStr:String, messageStr:String) -> Void {
        let alert = UIAlertController(title: titleStr, message: messageStr, preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Ok", comment: ""), style: UIAlertActionStyle.default, handler: nil))
        vc.present(alert, animated: true, completion: nil)
        
    }
    
    static func showGlobalProgressHUDWithTitle(_ vc: UIView,title: String) -> MBProgressHUD{
        
        let hud = MBProgressHUD.showAdded(to: vc, animated: true)
        hud.contentColor = UIColor.white
        // hud.backgroundView.alpha = 0.9
        hud.backgroundView.color = UIColor.black.withAlphaComponent(0.6)
        
        hud.bezelView.color = UIColor(red: 0, green: 0/255, blue: 0, alpha: 1)
        hud.label.text = title
        hud.label.font = UIFont(name: "HelveticaNeue", size: 15.0)!
        return hud
    }
    
    static func dismissGlobalHUD(_ vc: UIView) -> Void{
        
        MBProgressHUD.hide(for: vc, animated: true)
    }
    
   static func convertToDictionary(text: String) -> [String: Any]? {
        
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                //print(error.localizedDescription)
                return nil
            }
        }
        return nil
    }
    
   static func convertToArray(text: String) -> NSArray {
        
        if let data = text.data(using: String.Encoding.utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as! NSArray
            } catch {
                //print(error.localizedDescription)
                return []
            }
        }
        return []
    }
}
class StructConstants {
    
    struct BaseURL {
        
        static let loginUrl = "https://salestrackapi.azurewebsites.net/api/Account/Login"
        static let productUrl = "https://salestrackapi.azurewebsites.net/api/Products/GetAll"
        static let businessUrl = "https://salestrackapi.azurewebsites.net/api/Business/GetAll"
        static let selectBrandUrl = "https://salestrackapi.azurewebsites.net/api/Brand/GetAll"
        static let productCategoryUrl = "https://salestrackapi.azurewebsites.net/api/ProductCategory/GetAll"
        static let orderGetUrl = "https://salestrackapi.azurewebsites.net/api/Order/GetAll?Status=NEW"
        static let dealsGetUrl = "https://salestrackapi.azurewebsites.net/api/Deal/GetAllActive"

  }
}
