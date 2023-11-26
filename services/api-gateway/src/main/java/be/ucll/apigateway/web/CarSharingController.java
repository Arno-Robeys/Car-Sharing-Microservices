package be.ucll.apigateway.web;

import be.ucll.apigateway.api.TestApiDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CarSharingController implements TestApiDelegate {

    @Override
    public ResponseEntity<Void> test()
    {
        //Met deze methode kan je testen of de api-gateway werkt
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
