package br.com.fiap.unidades.resource;

import br.com.fiap.unidades.dto.reponse.ChefeResponse;
import br.com.fiap.unidades.dto.request.ChefeRequest;
import br.com.fiap.unidades.entity.Chefe;
import br.com.fiap.unidades.entity.Unidade;
import br.com.fiap.unidades.entity.Usuario;
import br.com.fiap.unidades.service.ChefeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/chefe")
public class ChefeResource implements ResourceDTO<ChefeRequest, ChefeResponse>{

    @Autowired
    private ChefeService service;

    @GetMapping
    public ResponseEntity<List<ChefeResponse>> findAll(
            @RequestParam(name="substituto", required = false) String substituto,
            @RequestParam(name="usuarioId", required = false) Long usuarioId,
            @RequestParam(name="unidadeId", required = false) Long unidadeId
    ) {

        Chefe.ChefeBuilder chefeBuilder = Chefe.builder();

        if(!Objects.isNull(substituto)) chefeBuilder.substituto(Boolean.parseBoolean(substituto));

        if (!Objects.isNull(usuarioId)) chefeBuilder.usuario(Usuario.builder().id(usuarioId).build());


        if (!Objects.isNull(unidadeId)) chefeBuilder.unidade(Unidade.builder().id(unidadeId).build());


        Chefe chefe = chefeBuilder.build();

        System.out.println(chefe.toString());

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase();

        Example<Chefe> example = Example.of(chefe, matcher);

        System.out.println(example.toString());

        List<ChefeResponse> list = service.findAll(example)
        .stream()
        .map(service::toResponse).toList();

        if(list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}")
    @Override
    public ResponseEntity<ChefeResponse> findById(@PathVariable Long id) {
        ChefeResponse response = service.toResponse(service.findById(id));
        if(Objects.isNull(response)) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(response);
    }

    @Transactional
    @PostMapping
    @Override
    public ResponseEntity<ChefeResponse> save(@RequestBody @Valid ChefeRequest request) {
        Chefe entity = service.toEntity(request);
        if(Objects.isNull(entity)) return ResponseEntity.badRequest().build();

        Chefe saved = service.save(entity);
        ChefeResponse response = service.toResponse(saved);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path( "/{id}" )
                .buildAndExpand( saved.getId() )
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

}
