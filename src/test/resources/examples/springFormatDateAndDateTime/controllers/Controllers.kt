package ie.zalando.controllers

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import java.time.OffsetDateTime
import kotlin.Int
import kotlin.Unit

@Controller
@Validated
@RequestMapping("")
interface ExampleController {
    /**
     *
     *
<<<<<<< HEAD
     * @param bDateTime
     * @param cInt
     * @param aDate
=======
     * @param aDate
     * @param bDateTime
     * @param cInt
>>>>>>> upstream/master
     */
    @RequestMapping(
        value = ["/example"],
        produces = [],
        method = [RequestMethod.GET]
    )
    fun get(
<<<<<<< HEAD
=======
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value = "aDate", required = true)
        aDate: LocalDate,
>>>>>>> upstream/master
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(
            value = "bDateTime",
            required =
            true
        )
        bDateTime: OffsetDateTime,
<<<<<<< HEAD
        @RequestParam(value = "cInt", required = true) cInt: Int,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value = "aDate", required = false)
        aDate: LocalDate?
=======
        @RequestParam(value = "cInt", required = true) cInt: Int
>>>>>>> upstream/master
    ): ResponseEntity<Unit>
}