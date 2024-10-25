//
//  BusinessTableViewCell.swift
//  SalesTrack
//
//  Created by Manish Behl on 27/09/18.
//  Copyright © 2018 Abineet. All rights reserved.
//

import UIKit

class BusinessTableViewCell: UITableViewCell {


    
    @IBOutlet weak var imgView: UIImageView!
    @IBOutlet weak var companyNameLbl: UILabel!
    @IBOutlet weak var addressLbl: UILabel!
    @IBOutlet weak var cityLbl: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
