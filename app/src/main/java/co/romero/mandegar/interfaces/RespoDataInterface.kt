package co.romero.mandegar.interfaces

import co.romero.mandegar.response.Respo

interface RespoDataInterface {

    fun data(response:Respo)
    fun status(msg: String?)

}