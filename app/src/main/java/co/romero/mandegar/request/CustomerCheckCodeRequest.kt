package co.romero.mandegar.request

class CustomerCheckCodeRequest(internal var code: Int,internal var mobile: String?="",internal var email: String?="",internal var reg_id:String)