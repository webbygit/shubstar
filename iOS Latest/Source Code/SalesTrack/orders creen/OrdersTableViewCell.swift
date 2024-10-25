//
//  OrdersTableViewCell.swift
//  SalesTrack
//
//  Created by Manish Behl on 27/09/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import UIKit

class OrdersTableViewCell: UITableViewCell {

    @IBOutlet weak var businessName: UILabel!
    @IBOutlet weak var businessType: UILabel!
    @IBOutlet weak var orderNumber: UILabel!
    @IBOutlet weak var totalAccount: UILabel!
    @IBOutlet weak var orderOn: UILabel!

    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
