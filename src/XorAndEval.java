import java.io.IOException;
import java.io.InputStream;

class XorAndEval {

    private int lookaheadToken;
    private InputStream in;

    public XorAndEval(InputStream in) throws IOException {
		this.in = in;
		lookaheadToken = in.read();
    }

    private void consume(int symbol) throws IOException, ParseError {
		if (lookaheadToken != symbol)
			throw new ParseError();
		lookaheadToken = in.read();
    }

    private int evalDigit(int digit){
		return digit - '0';
    }

    private int T_exp() throws IOException, ParseError {
		int term = T_term();
		return T_xor(term);
    }

    private int T_xor(int term) throws IOException, ParseError {
        if (lookaheadToken != '^')
            return term;
        consume(lookaheadToken);
        int rightNum = T_term();
        int rightPart = T_xor(rightNum);
        return term ^ rightPart;
    }

	private int T_term() throws IOException, ParseError {
		int factor = T_factor();
		return T_and(factor);
	}

	private int T_and(int term) throws IOException, ParseError {
		if (lookaheadToken != '&')
            return term;
		consume(lookaheadToken);
		int rightNum = T_factor();
		int rightPart = T_and(rightNum);
		return term & rightPart;
	}

	private int T_factor() throws IOException, ParseError {
		if (lookaheadToken == '(') {
            consume(lookaheadToken);
            int num = T_exp();
            if (lookaheadToken != ')')
                throw new ParseError();
            consume(lookaheadToken);
            return num;
        } else if (lookaheadToken >= '0' && lookaheadToken <= '9') {
            int num = evalDigit(lookaheadToken);
            consume(lookaheadToken);
            return num;
        }
		throw new ParseError();
	}

    public int eval() throws IOException, ParseError {
		int rv = T_exp();
		if (lookaheadToken != '\n' && lookaheadToken != -1)
			throw new ParseError();
		return rv;
    }

    public static void main(String[] args) {
		try {
			XorAndEval evaluate = new XorAndEval(System.in);
			System.out.println(evaluate.eval());
		}
		catch (IOException | ParseError e) {
			System.err.println(e.getMessage());
		}
    }

}

/* LL(1) grammar:
	exp    -> term xor
	xor    -> ^ term xor
		    | e
	term   -> factor and
	and    -> & factor and
		 	| e
	factor -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
			| (exp)
 */
