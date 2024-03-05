package godeck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import godeck.models.QueueResponse;
import godeck.services.QueueService;

/**
 * Controller that handles the queue of users waiting for a game. It is
 * responsible for manipulating the queue from http requests.
 * 
 * Is a controller. Can be accessed from the web.
 * 
 * @author Bruno Pena Baeta
 */
@Controller
@RequestMapping("/queue")
public class QueueController {
    private QueueService queueService;

    @Autowired
    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PostMapping(path = "")
    @ResponseBody
    public QueueResponse queue(@RequestBody String stringUserId) {
        return queueService.queue(stringUserId);
    }

    @PostMapping(path = "/dequeue")
    @ResponseBody
    public QueueResponse dequeue(@RequestBody String stringUserId) {
        return queueService.dequeue(stringUserId);
    }
}
