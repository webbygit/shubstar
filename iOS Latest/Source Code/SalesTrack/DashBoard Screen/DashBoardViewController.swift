//
//  DashBoardViewController.swift
//  SalesTrack
//
//  Created by Manish Behl on 27/09/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import UIKit

class DashBoardViewController: UIViewController {
    
    
    var items = ["MY GOALS","ORDERS","PRODUCTS","BUSINESS","Chat","Deals"]
    var itemsImages = ["my_goals","orders","products","business","chat","deals"]

    @IBOutlet weak var monthlyTargetValue: UILabel!
    @IBOutlet weak var achivedTargetValue: UILabel!
    @IBOutlet weak var startEndView: UIView!
    @IBOutlet weak var endDayLbl: UILabel!
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var timeLbl: UILabel!
    @IBOutlet weak var collectionView: UICollectionView!
    
    var timeBtnPres : Bool!

    override func viewDidLoad() {
        super.viewDidLoad()
        timeBtnPres = true

        // Do any additional setup after loading the view.
        
    }
    
    
    
    
    @IBAction func bckBtnClicked(_ sender: UIButton) {
      //  SlideNavigationController.sharedInstance().toggleLeftMenu()
    }

    @IBOutlet weak var timeBtnPressed: UIButton!
    @IBAction func startBtnTime(_ sender: UIButton) {
        if(timeBtnPres == true) {
            
          

            timeBtnPres = false
        } else {
           
            timeBtnPres = true
        }
        
        
        
    }
    
 
 
    @IBAction func notificationAlertClicked(_ sender: UIButton) {
    }
}






extension DashBoardViewController : UICollectionViewDelegate,UICollectionViewDataSource{
    
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.items.count
    }
    
    // make a cell for each cell index path
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        // get a reference to our storyboard cell
        let cell :DashboardCell = collectionView.dequeueReusableCell(withReuseIdentifier: "cell", for: indexPath as IndexPath) as! DashboardCell
        
        cell.itemsLabel.text = self.items[indexPath.item]
        cell.itemsImageView.image = UIImage(named: itemsImages[indexPath.row])
        return cell
    }
    
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        // handle tap events
        print("You selected cell #\(indexPath.item)!")
        
        if indexPath.item == 0 {
            
            let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "MyGoalsViewController") as? MyGoalsViewController
            self.navigationController?.pushViewController(vc!, animated: true)
        }else if indexPath.item == 1 {
            let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "OrdersViewController") as? OrdersViewController
            self.navigationController?.pushViewController(vc!, animated: true)
            
        }else if indexPath.item == 2 {
            let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ProductViewController") as? ProductViewController
            self.navigationController?.pushViewController(vc!, animated: true)
            
        }else if indexPath.item == 3 {
            let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "BusinessViewController") as? BusinessViewController
            self.navigationController?.pushViewController(vc!, animated: true)
            
        }else if indexPath.item == 5 {
            
            let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "DealsViewController") as? DealsViewController
            self.navigationController?.pushViewController(vc!, animated: true)

        }
        
        
        
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        let size = CGSize(width: 0.28 * self.view.frame.width, height: (0.28 * self.view.frame.width) + 60)
        print(size)
        return size
        
        
        
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return UIEdgeInsetsMake(0, 0, 0, 0)
        
    }
    func collectionView(_ collectionView: UICollectionView,layout collectionViewLayout: UICollectionViewLayout,                        minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 1.0
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout,                      minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 1.0
    }
}



