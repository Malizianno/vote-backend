package ro.cristiansterie.vote.util;

import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ro.cristiansterie.vote.entity.CandidateDAO;
import ro.cristiansterie.vote.entity.UserDAO;

public class EntityHelper {
    private static final String[] FIRST_NAMES = new String[] { "Mihai", "Ana", "Andrei", "Adela", "George", "Andreea",
            "Cristian", "Angela", "Ion", "Bianca", "Paul", "Ștefania", "Ștefan", "Anca", "Alexandru", "Vasilica",
            "Petru", "Veta", "Florin" };
    private static final String[] LAST_NAMES = new String[] { "Anghelescu", "Popescu", "Aftenie", "Ionescu",
            "Adumitroaiei", "Dîrlău", "Gabor", "Decusară", "Iliescu", "Deju", "Petrescu", "Hârlișcă", "Chiricheș",
            "Halbeș", "Kogălniceanu", "Kiriță", "Protopopescu", "Deaconu", "Pamfil", "Pretop", "Oală", "Similea" };

    private static Random random = new Random();

    private EntityHelper() {
        // empty private constructor
    }

    public static List<CandidateDAO> generateFakeCandidates(int electionID) {
        List<CandidateDAO> returnable = new ArrayList<>();

        PartyTypeEnum[] parties = PartyTypeEnum.values();

        for (int i = 1; i < parties.length; i++) {
            returnable.add(generateCandidateDAO(parties[i], electionID));
        }

        return returnable;
    }

    public static List<UserDAO> generateFakeUsers(int no) {
        List<UserDAO> returnable = new ArrayList<>();
        long startNumber = 1234567890000L;

        for (int i = 0; i < no; i++) {
            returnable.add(generateUserDAO(startNumber + i));
        }

        return returnable;
    }

    private static CandidateDAO generateCandidateDAO(PartyTypeEnum party, int electionID) {
        CandidateDAO dto = new CandidateDAO();

        dto.setFirstName(FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]);
        dto.setLastName(LAST_NAMES[random.nextInt(LAST_NAMES.length)]);
        dto.setParty(party);
        dto.setElectionId(electionID);
        dto.setDescription(
                "Numele meu este " + dto.getFirstName() + " " + dto.getLastName()
                        + (PartyTypeEnum.IND.equals(dto.getParty())
                                ? " și sunt "
                                : " și fac parte din partidul ")
                        + dto.getParty()
                        + " și particip anul acesta la alegerile prezidențiale, cu dorința de a schimba România!");
        dto.setImage("https://picsum.photos/200/300");

        return dto;
    }

    private static UserDAO generateUserDAO(Long cnp) {
        UserDAO dto = new UserDAO();
        Date now = new Date();

        dto.setRole(UserRoleEnum.VOTANT);
        dto.setHasVoted(false);
        dto.setFirstname(String.valueOf(cnp));
        dto.setLastname(String.valueOf(cnp));
        dto.setGender(cnp % 2 == 0 ? UserGenderEnum.MALE : UserGenderEnum.FEMALE);
        dto.setCnp(cnp);
        dto.setNationality(UserNationalityEnum.ROMANIAN);
        dto.setIdSeries("XX");
        dto.setIdNumber(Integer.valueOf(String.valueOf(cnp).substring(0, 6)));
        dto.setResidenceAddress("Residence Address");
        dto.setValidityStartDate(now.getTime());
        dto.setValidityEndDate(now.getTime());

        dto.setUsername(null);
        dto.setPassword(null);
        dto.setFaceImage(null);
        dto.setIdImage(null);

        return dto;
    }
}
