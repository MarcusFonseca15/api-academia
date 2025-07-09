package sync.fit.api.model;

import jakarta.persistence.*;
import lombok.Data; // Pode manter @Data se quiser, ou usar @Getter, @Setter
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; // Remover este se você adicionar construtores específicos

@Entity
@Table(name = "funcionario") // A tabela principal que conterá todos os tipos de funcionários
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Define a estratégia de herança
@DiscriminatorColumn(name = "tipo_funcionario", discriminatorType = DiscriminatorType.STRING) // Coluna que dirá o tipo
@Data // Usando @Data para simplicidade, mas pode ser @Getter @Setter
@NoArgsConstructor // Necessário para JPA
public abstract class Funcionario { // Mudar para abstract, pois Funcionario não será instanciado diretamente

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    @Column(nullable = false)
    private Double salario;

    // Construtor para ser usado pelas subclasses
    public Funcionario(Long id, String nome, String email, Cargo cargo, Double salario) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
        this.salario = salario;
    }

    // Se usar Lombok @AllArgsConstructor na classe, ele já geraria este construtor.
    // Mas como a classe se torna 'abstract' e pode ter mais construtores nas subclasses,
    // é bom ter um construtor explícito aqui ou controlar com @AllArgsConstructor se Lombok estiver configurado para isso.
}