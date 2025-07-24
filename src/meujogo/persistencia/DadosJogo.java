package meujogo.persistencia;

import meujogo.modelo.Alma;
import meujogo.modelo.God;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DadosJogo implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Alma> almas;
    private final God jogador;

    public DadosJogo(God jogador, List<Alma> almas) {
        // Null checks for required fields
        this.jogador = Objects.requireNonNull(jogador, "Jogador não pode ser nulo");
        
        // Defensive copy of the list and null check
        Objects.requireNonNull(almas, "Lista de almas não pode ser nula");
        this.almas = new ArrayList<>(almas);
    }
    
    // Returns an unmodifiable view of the list to prevent external modifications
    public List<Alma> getAlmas() {
        return Collections.unmodifiableList(almas);
    }

    public God getJogador() {
        return jogador;
    }

    // Optional: Override equals and hashCode for better object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DadosJogo dadosJogo = (DadosJogo) o;
        return Objects.equals(almas, dadosJogo.almas) &&
               Objects.equals(jogador, dadosJogo.jogador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(almas, jogador);
    }
}