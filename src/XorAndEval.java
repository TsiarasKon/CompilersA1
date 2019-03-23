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
    	// TODO: add parenthesis
		int term = T_term();
		return term;//T_xor(term);
    }

//	private int T_xor(int term) throws IOException, ParseError {
//		if (lookaheadToken < '0' || lookaheadToken > '9')
//			throw new ParseError();
//		int num = evalDigit(lookaheadToken);
//		consume(lookaheadToken);
//		return T_term(num);
//	}

	private int T_term() throws IOException, ParseError {
		int factor = T_factor();
		return T_and(factor);
	}

	private int T_and(int term) throws IOException, ParseError {
		if (lookaheadToken == '\n' || lookaheadToken == -1)
			return term;
		if (lookaheadToken != '&')
			throw new ParseError();
		consume(lookaheadToken);
		int rightNum = T_factor();
		int rightPart = T_and(rightNum);
		return term & rightPart;
	}

	private int T_factor() throws IOException, ParseError {
		// TODO: add parenthesis
		if (lookaheadToken < '0' || lookaheadToken > '9')
			throw new ParseError();
		int num = evalDigit(lookaheadToken);
		consume(lookaheadToken);
		return num;
	}

//    private int T_factor(int cond) throws IOException, ParseError {
//		if (lookaheadToken == ':' || lookaheadToken == '\n' || lookaheadToken == -1)
//			return cond;
//		if (lookaheadToken != '?')
//			throw new ParseError();
//		consume('?');
//		int thenPart = T_exp();
//		consume(':');
//		int elsePart = T_exp();
//		return cond != 0 ? thenPart : elsePart;
//    }

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
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
		catch(ParseError err){
			System.err.println(err.getMessage());
		}
    }
}

/* CFG:
	exp    -> term xor
	xor    -> ^ term xor
		    | e
	term   -> factor and
	and    -> & factor and
		 	| e
	factor -> 0|1|2|3|4|5|6|7|8|9
			| (exp)
 */