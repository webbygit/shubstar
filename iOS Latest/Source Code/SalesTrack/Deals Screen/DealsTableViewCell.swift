//
//  DealsTableViewCell.swift
//  SalesTrack
//
//  Created by Manish Behl on 05/10/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import UIKit

class DealsTableViewCell: UITableViewCell {

    @IBOutlet weak var discountLbl: UILabel!
    @IBOutlet weak var applicationPercentageLbl: UILabel!
    @IBOutlet weak var validDateLbl: UILabel!
    @IBOutlet weak var endDateLbl: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
