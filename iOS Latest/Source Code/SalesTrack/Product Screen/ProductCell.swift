//
//  ProductCell.swift
//  SalesTrack
//
//  Created by Alok Yadav on 9/28/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import UIKit

class ProductCell: UITableViewCell {

    @IBOutlet weak var phoneNameLbl: UILabel!
    @IBOutlet weak var priceLbl: UILabel!
    @IBOutlet weak var descriptionLbl: UILabel!

    @IBOutlet weak var addToCartBtn: UIButton!
    
    
    
    @IBOutlet weak var imgView: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
