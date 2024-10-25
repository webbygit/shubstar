//
//  ViewController.swift
//  SalesTrack
//
//  Created by Alok Yadav on 9/27/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import UIKit
import GoogleMaps
import CoreLocation
import MBProgressHUD
import Alamofire

class ViewController: UIViewController,CLLocationManagerDelegate {

    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var signInOutlet: UIButton!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var emailTopHight: NSLayoutConstraint!
    let locationManager = CLLocationManager()
    @IBOutlet var mapView: GMSMapView!
    @IBOutlet weak var passwordEyeOutlet: UIButton!

    @IBOutlet weak var alertDisplay: UILabel!
    var iconClick : Bool!

    override func viewDidLoad() {
        super.viewDidLoad()
       //emailTopHight.constant
        emailTextField.text = "anmolg@gmail.com"
            passwordTextField.text = "AnmolG1!"

        signInOutlet.layer.cornerRadius = 15
        iconClick = true
        
        if UIDevice().userInterfaceIdiom == .phone {
            switch UIScreen.main.nativeBounds.height {
            case 1136:
                print("iPhone 5 or 5S or 5C")
            case 1334:
                emailTopHight.constant = 50
                print("iPhone 6/6S/7/8")
            case 2208:
                emailTopHight.constant = 60
                print("iPhone 6+/6S+/7+/8+")
            case 2436:
                emailTopHight.constant = 70
            case 2688:
                emailTopHight.constant = 80
                print("iPhone X")
            case 1792:
                emailTopHight.constant = 70
                print("iPhone X")
            default:
                print("unknown")
            }
        }
            self.googleMapCurrent()
    }

    @IBAction func signInBtnAction(_ sender: UIButton) {
        guard self.emailTextField.text?.isEmpty == false else {
            self.alertDisplay.text = "Please enter email address!"
            return
      
        }
        guard self.passwordTextField.text?.isEmpty == false else {
            self.alertDisplay.text = "Please enter password!"
            return
        }
        CommanClass.showGlobalProgressHUDWithTitle(self.view, title: "")
        let uuid = UserDefaults.standard.string(forKey: "uuidKey")

        let parameters = ["Username": self.emailTextField.text ?? "", "Password": self.passwordTextField.text ?? "","DeviceId": uuid ?? "", "DeviceType": "ios"
            ?? "" ]//, "Longitude": 0.0 ?? "","Latitude": 0.0 ?? ""]
        
        if let theJSONData = try? JSONSerialization.data(withJSONObject: parameters,options: []) {
            let theJSONText = String(data: theJSONData,encoding: .ascii)
            print("JSON string = \(theJSONText!)")
        }
        
        Alamofire.request(StructConstants.BaseURL.loginUrl, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: nil)
            .responseJSON { response in
                print(response.request as Any)  // original URL request
                print(response.response as Any) // URL response
                print(response.result.value as Any)   // result of response serialization
            
            guard response.result.error == nil,let _ = response.result.value else{
                
              // CommanClass.showAlertMessage(self,titleStr:"Alert", messageStr:"Server Error")
                 self.alertDisplay.text = "Server Error"

                CommanClass.dismissGlobalHUD(self.view)
                return
            }
                
                let jsonResponce = response.result.value
                
                print(jsonResponce!)
                if jsonResponce is NSDictionary{
                let msg = jsonResponce as! NSDictionary
                if let mesage = msg.value(forKey: "Message")  as? String{
                    
                    self.alertDisplay.text = mesage as! String
                    CommanClass.dismissGlobalHUD(self.view)

                } else {
                    let accessToken = msg.value(forKey: "access_token")  as! String
                    UserDefaults.standard.set(accessToken, forKey: "accessToken")
                    self.navigateToDashboard()
                    CommanClass.dismissGlobalHUD(self.view)

                }
            }


        }
    }
    func navigateToDashboard(){
        
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "DashBoardViewController") as? DashBoardViewController
        self.navigationController?.pushViewController(vc!, animated: true)

//        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
//        let newViewController = storyBoard.instantiateViewController(withIdentifier: "DashBoardViewController") as! DashBoardViewController
//        self.present(newViewController, animated: true, completion: nil)


    }
    @IBAction func passwordEyeClicked(_ sender: UIButton) {
        if(iconClick == true) {
            passwordTextField.isSecureTextEntry = true
            iconClick = false
        } else {
            passwordTextField.isSecureTextEntry = false
            iconClick = true
        }
    }
 }

extension ViewController : GMSMapViewDelegate{
    
    func googleMapCurrent(){
        
        /// Google Map
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestAlwaysAuthorization()
        locationManager.startUpdatingLocation()
        
        if CLLocationManager.locationServicesEnabled() {
            switch (CLLocationManager.authorizationStatus()) {
            case .notDetermined, .restricted, .denied:
                print("No access")
            case .authorizedAlways, .authorizedWhenInUse:
                print("Access")
            }
        } else {
            print("Location services are not enabled")
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let newLocation = locations.last // find your device location
       mapView.camera = GMSCameraPosition.camera(withTarget: newLocation!.coordinate, zoom: 14.0) // show your device location on map
        mapView.settings.myLocationButton = true // show current location button
        let lat = (newLocation?.coordinate.latitude)! // get current location latitude
        let long = (newLocation?.coordinate.longitude)! //get current location longitude
        let center = CLLocationCoordinate2D(latitude: lat, longitude: long)
        let marker = GMSMarker()
        marker.position = center
        marker.title = "current location"
        marker.map = mapView
        
    }
}
