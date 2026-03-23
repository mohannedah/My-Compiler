package compiler.Lexer.Tokens;

import java.util.Objects;

public class Identifier extends Token {
    public Identifier(String token) {
        super(token);
    }

    @Override
    public boolean equals(Object otherIdentifier) {
        if (this == otherIdentifier) return true;
        if (otherIdentifier == null || getClass() != otherIdentifier.getClass()) return false;
        Identifier that = (Identifier) otherIdentifier;
        return Objects.equals(this.token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.token);
    }
}
