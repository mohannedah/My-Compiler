package compiler.Lexer;

import java.util.AbstractMap.SimpleEntry;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Stack;

import compiler.Constants;
import compiler.DataStructures.Trie;
import compiler.Lexer.TokenFactories.KeywordFactory;
import compiler.Lexer.TokenFactories.OperatorFactory;
import compiler.Lexer.TokenFactories.TokenFactory;
import compiler.Lexer.TokenFactories.TypeFactory;
import compiler.Lexer.Tokens.*;


public class CompilerLexer {
    public Trie<TokenFactory> trie;
    private PushbackReader inputReader;
    private Stack<State> states;

    public CompilerLexer(PushbackReader input) {
        this.trie = new Trie<TokenFactory>(this.getEntries());
        this.inputReader = input;
        this.states = new Stack<>();
        this.states.push(new State(0, 0));
    };

    private LinkedList<SimpleEntry<String, TokenFactory>> getEntries() 
    {
        LinkedList<SimpleEntry<String, TokenFactory>> list = new LinkedList<>();
        TokenFactory typeFactory = new TypeFactory(), keywordFactory = new KeywordFactory(), operatorFactory = new OperatorFactory();
        for(int i = 0; i < Constants.KEYWORDS.length; i++) 
        {
            list.add(new SimpleEntry<>(Constants.KEYWORDS[i], keywordFactory));
        };

        for(int i = 0; i < Constants.TYPES.length; i++) 
        {
            list.add(new SimpleEntry<>(Constants.TYPES[i], typeFactory));
        };

        for(int i = 0; i < Constants.OPERATORS.length; i++) 
        {
            list.add(new SimpleEntry<>(Constants.OPERATORS[i], operatorFactory));
        };
        return list;
    };

    private boolean isNewLine(char currChar) 
    {
        return currChar == '\n';
    }

    private boolean isObjectAccessor(char currChar) 
    {
        return currChar == '.';
    }

    private State getPrevState() 
    {
        assert !states.isEmpty(); // States cannot be empty as it will always contain a dummy `State` in the beginning, we will only pop elements we pushed in the reset method.
        return states.getLast();
    };

    private void registerNewState(char currChar) 
    {
        State prevState = getPrevState();
        int currLineNumber = -1, currColNumber = -1; 

        if(isNewLine(currChar)) {
            currLineNumber = prevState.lineNumber + 1;
            currColNumber = 1;
        } else {
            currLineNumber = prevState.lineNumber;
            currColNumber = prevState.colNumber + 1; 
        }

        states.push(new State(currLineNumber, currColNumber));
        return;
    }

    private void reset(char currChar) throws IOException
    {
        this.inputReader.unread(currChar);
        assert !this.states.isEmpty(); // The assumption here that the stack is not empty in this case.
        this.states.pop();
        return;
    }

    private char getNextChar() throws IOException, NoSuchElementException
    {
        char currChar = (char)this.inputReader.read();

        if((int)currChar == Constants.END_OF_INPUT) throw new NoSuchElementException("End of the stream");
        
        registerNewState(currChar);

        return currChar;
    }

    private boolean isWhiteSpaceChar(char currChar) 
    {
        for(int i = 0; i < Constants.WHITE_SPACE_CHARACTERS.length; i++) {
            if(currChar == Constants.WHITE_SPACE_CHARACTERS[i]) return true;
        };
        return false;
    };

    private void skipWhiteSpaces() throws IOException, NoSuchElementException
    {
        char currChar = this.getNextChar();
        while(isWhiteSpaceChar(currChar)) {
            currChar = this.getNextChar();
        };
        this.reset(currChar);
    }

    private boolean isSperator(char currChar) 
    {
        for(int i = 0; i < Constants.SEPERATORS.length; i++) {
            if(currChar == Constants.SEPERATORS[i]) return true;
        }
        return false;
    };

    private boolean isQuoteCharacter(char currChar) 
    {
        return currChar == '"';
    }

    private boolean isDigit(char currChar) 
    {
        return currChar >= '0' && currChar <= '9';
    }

    private boolean isLowerCase(char currChar) 
    {
        return currChar >= 'a' && currChar <= 'z';
    };

    private boolean isUpperCase(char currChar) 
    {
        return currChar >= 'A' && currChar <= 'Z';
    };

    private boolean isValidIdentifierChar(char currChar) 
    {
        return isDigit(currChar) || isLowerCase(currChar) || isUpperCase(currChar) || currChar == '_';
    };

    private boolean isSpecialCharacter(char currChar) 
    {
        for(int i = 0; i < Constants.SPECIAL_CHARACTERS.length; i++) 
        {
            if(Constants.SPECIAL_CHARACTERS[i] == currChar) return true;
        };
        return false;
    };

    // TODO: adapt this method to throw an exception.
    private Token scanNumber(String currNumber) throws IOException, Exception
    {
        char currChar = this.getNextChar();
        while(!(this.isSperator(currChar) || this.isWhiteSpaceChar(currChar))) 
        {
            if(!isDigit(currChar) || currChar != '.') throw new Exception("Invalid number");
            currNumber += currChar;
        };
        this.reset(currChar);
        return new NumberToken(currNumber);
    };

    private Token scanSpecialOperator(String currString) throws IOException, Exception 
    {   
        try {
            char currChar = this.getNextChar();
            while(isSpecialCharacter(currChar)) 
            {
                currString += currChar;
                currChar = this.getNextChar();
            };
            this.reset(currChar);
        } catch (NoSuchElementException e) {
            // Tolerate this exception in the method. We assume here another read from the reader will throw this exception anyways.
        } 
        // At this point we know that `currString` holds an Operator and we know that `this.trie` contains the operator.
        if(!this.trie.containsWord(currString)) return null;

        return this.trie.getWord(currString).create(currString);
    };

    private Token scanComments(String currString) throws IOException, Exception 
    {
        try {
            char currChar = this.getNextChar();
            while(!this.isWhiteSpaceChar(currChar)) 
            {
                currString += currChar;
                currChar = this.getNextChar();
            };
        } catch (NoSuchElementException e) {
            // TODO: handle exception
        }
        return new Comment(currString);
    }

    private Token tryScanIdentifier(String currIdentifier) throws IOException
    {
        /* 
            This method tries to return a token of type identifer unless it encountered a key found in `this.trie`.
            Formally it will return one of the following Tokens ->
                - new Identifer();
                - new Keyword();
                - new Operator();
                - new Type(); 
        */     
        try {
            char currChar = this.getNextChar();
            while(this.isValidIdentifierChar(currChar)) 
            {
                currIdentifier += currChar;
                currChar = this.getNextChar();
            };   
            this.reset(currChar);
        } catch (NoSuchElementException e) {
            // Tolerate this exception in the method. We assume here another read from the reader will throw this exception anyways.
        }

        TokenFactory trieToken = this.trie.getWord(currIdentifier);

        if(trieToken != null) {
            return trieToken.create(currIdentifier);
        }

        return new Identifier(currIdentifier);      
    };

    private Token scanString(String currString) throws Exception, IOException
    {
        try {
            char currChar = this.getNextChar();
            while (!isQuoteCharacter(currChar)) {
                currString += currChar;
                currChar = this.getNextChar();
            }
        } catch (NoSuchElementException e) {
            throw new Exception("Invalid identifier: Most likely you missed an ending Quote character");
        }
        return new StringToken(currString);
    };

    public Token nextToken() throws IOException, Exception
    {
        skipWhiteSpaces();

        char currChar = getNextChar();
        Token currToken = null;

        String currString = String.format("%c", currChar);
        State tokenState = this.getPrevState();

        if(isDigit(currChar)) {
            currToken = this.scanNumber(currString);
        } else if (isValidIdentifierChar(currChar)) {
            currToken = this.tryScanIdentifier(currString);
        } else if (isSperator(currChar)) {
            currToken = new Seperator(currString);
        } else if (isQuoteCharacter(currChar)) {
            currToken = this.scanString(currString);
        } else if (isSpecialCharacter(currChar)) {
            currToken = this.scanSpecialOperator(currString);
        } else if(isObjectAccessor(currChar)) {
            currToken = new ObjectAccessor(".");
        } else if(currChar == '#') 
        {
            currToken = this.scanComments(currString);
        };
        currToken.state = tokenState;
        return currToken;
    };
}
