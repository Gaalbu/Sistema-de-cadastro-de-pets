import java.time.LocalDate;
import java.time.LocalTime;

public class tempoCadastroPet {
    //ano, mês, dia
    //20231101T1234
    public String AnoMesDia(){
        LocalDate hoje = LocalDate.now();
        String output = "";
        
        output = hoje.getYear() + "" + hoje.getMonthValue() + "" + hoje.getDayOfMonth();
        return output;
    }
    public String Horario(){
        LocalTime horarioAgora = LocalTime.now();
        String output = "";
        output = horarioAgora.getHour()+""+horarioAgora.getMinute();
        return output;
    }
    


}
