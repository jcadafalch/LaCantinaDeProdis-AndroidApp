package cat.copernic.prodis.lacantinadeprodis.model

import java.util.Date

data class dtclss_cuiner(
    var comandaComencada: Boolean? = null,
    var comandaId: String ?= null,
    var comandaPagada: Boolean ?= null,
    var date: Date ?= null,
    var preparat: Boolean ?= null,
    var preuTotal: Int ?= null,
    var user: String ?= null,
    var visible: Boolean ?= null
)