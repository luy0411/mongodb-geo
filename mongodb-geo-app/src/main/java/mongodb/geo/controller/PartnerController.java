package mongodb.geo.controller;

import mongodb.geo.domain.Partner;
import mongodb.geo.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partners")
public class PartnerController {

    private PartnerService service;

    @Autowired
    public PartnerController(final PartnerService service){
        this.service = service;
    }

    @PostMapping
    public Partner save(@RequestBody final Partner partner){
        return service.save(partner);
    }

    @GetMapping("/{id}")
    public Partner find(@PathVariable final String id){
        return service.find(id);
    }

    @GetMapping("/closerTo")
    public Partner closerTo(@RequestParam("longitude") final Double longitude,
                            @RequestParam("latitude") final Double latitude){
        return service.findPartnerCloserTo(latitude, longitude);
    }

}
