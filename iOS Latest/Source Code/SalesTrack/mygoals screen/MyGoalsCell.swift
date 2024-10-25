//
//  MyGoalsCell.swift
//  SalesTrack
//
//  Created by Alok Yadav on 9/28/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import UIKit

class MyGoalsCell: UITableViewCell {

    @IBOutlet weak var companyNameLbl: UILabel!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var quantityLbl: UILabel!
    @IBOutlet weak var cityLbl: UILabel!
    @IBOutlet weak var amountLbl: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
