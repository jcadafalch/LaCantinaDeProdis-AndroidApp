package cat.copernic.prodis.lacantinadeprodis.model

import java.sql.Timestamp

data class dtclss_cuiner(
    var comandaComencada: Boolean? = null,
    var comandaId: String ?= null,
    var comandaPagada: Boolean ?= null,
    var date: Timestamp ?= null,
    var preparat: Boolean ?= null,
    var preuTotal: Int ?= null,
    var user: String ?= null,
    var visible: Boolean ?= null
)