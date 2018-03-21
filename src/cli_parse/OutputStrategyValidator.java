package cli_parse;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import static cli_parse.MainCLIParameters.ASCII;
import static cli_parse.MainCLIParameters.HTML;
import static cli_parse.MainCLIParameters.LATEX;

public class OutputStrategyValidator implements IParameterValidator {
    @Override
    public void validate(String s, String value) throws ParameterException {
        try {
            int i = Integer.parseInt(value);
            if (i != HTML && i != ASCII && i != LATEX) throw new ParameterException("Integer passed in for '-f' is not valid");
        }
        catch (NumberFormatException e) {
            throw new ParameterException("Parameter passed in was not an integer");
        }
    }
}
