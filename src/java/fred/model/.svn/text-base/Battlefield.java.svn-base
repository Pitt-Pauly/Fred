package fred.model;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.Random;

public class Battlefield implements Serializable {

    private static final long serialVersionUID = 1L;

		private int defenders;
        private ArrayList<Integer> initialAttackers = new ArrayList<Integer>();
        private int attackers;
        private boolean finished;
        private boolean success;
        private boolean cleared;
        private boolean withdrawn;
        private List<Territory> sources = new ArrayList<Territory>();
        private Territory target;
        private Player attacker;
        private Player defender;
        private Die die = new Die();
        private AttackRecord record;
        
        protected Battlefield() {
        	clearBattlefield();
        }

        public void setup(Player attacker, Player defender, ArrayList<Integer> initialAttackers, int defenders, ArrayList<Territory> sources, Territory target){
            this.initialAttackers = initialAttackers;
            this.defenders = defenders;
            this.sources = new ArrayList<Territory>(sources);
            this.target = target;
            finished = false;
            success = false;
            cleared = false;
            withdrawn = false;
            this.attacker = attacker;
            this.defender = defender;
            
                //Pools Units from source territories and removing them from source
            int totalNumberOfUnits = 0;
            for (Integer units: initialAttackers) {
              	totalNumberOfUnits = units + totalNumberOfUnits;
            }
            this.attackers = totalNumberOfUnits;
            for (int index = 0; index < sources.size(); index++) {
        	sources.get(index).setUnitCount(sources.get(index).getUnitCount() - initialAttackers.get(index));
            }
                //Pooling done

            record = new AttackRecord(attacker, defender, sources, 
                    target, initialAttackers, defenders);
        }

        public Die getDie() {
            return die;
        }
        
        void setDie(Die die) {
            this.die = die;
        }

        public int getDefenders() {
            return defenders;
        }

        public int getAttackers() {
            return attackers;
        }

        public ArrayList<Integer> getInitialAttackers() {
            return initialAttackers;
        }

        public List<Territory> getSources() {
            return sources;
        }

        public Territory getTarget() {
            return target;
        }

        public Player getAttacker() {
            return attacker;
        }

        public Player getDefender() {
            return defender;
        }
        
        public AttackRecord getRecord() {
        	return record;
        }

        public boolean isCleared(){
            return cleared;
        }

        public boolean isFinished(){
            return finished;
        }

        public boolean isSuccess(){
            return success;
        }
        
        public boolean isWithdrawn(){
        	return withdrawn;
        }

        public void performFullAttack() {
            while (finished == false) {
                attackRound();
                if(attackers == 0) {
                    finished = true;
                    record.setFinished(true);
                    success = false;
                    record.setSuccess(false);
                    record.setSurvivors(defenders);
                }
                if (target.getOwner() == attacker) {
                    success = true;
                    record.setSuccess(true);
                    finished = true;
                    record.setFinished(true);
                    record.setSurvivors(attackers);
                }
            }
        }

        public void performSingleAttack() {
            attackRound();
            if(attackers == 0) {
                finished = true;
                record.setFinished(true);
                success = false;
                record.setSuccess(false);
            }
            record.setSurvivors(defenders);
            if (target.getOwner() == attacker) {
                success = true;
                record.setSuccess(true);
                finished = true;
                record.setFinished(true);
                record.setSurvivors(attackers);
            }
        }

        private void attackRound() {
            int attackDice = Math.min(getAttackers(), 3);
            int defenceDice = Math.min(getDefenders(), 2);

            List<Integer> attackRolls = new ArrayList<Integer>();
            List<Integer> defenceRolls = new ArrayList<Integer>();

            for (int i = 0; i < attackDice; i++) {
                attackRolls.add(die.roll());
            }
            for (int i = 0; i < defenceDice; i++) {
                defenceRolls.add(die.roll());
            }

            Collections.sort(attackRolls, Collections.reverseOrder());
            Collections.sort(defenceRolls, Collections.reverseOrder());
            for (int i = 0; i < Math.min(attackDice, defenceDice); i++) {
                if (attackRolls.get(i) <= defenceRolls.get(i)) {
                    attackers--;
                }
                else {
                    if (target.getUnitCount() > 1) {
                        defenders--;
                        target.setUnitCount(defenders);
                    }
                    else {
                    	defenders = 0;
                        target.setOwner(attacker);
                        target.setUnitCount(attackers);
                    }
                }
            }
            record.addRound(attackers, defenders, attackRolls, defenceRolls);
        }

        public void withdraw() {
            List<Integer> returnedUnits = new ArrayList<Integer>();
            for (int index = 0; index < sources.size(); index++) {
            	returnedUnits.add(0);
            }
            if (returnedUnits.size() == 1) {
                returnedUnits.set(0, getAttackers());
            } else {
                for (int index = 0; index < getAttackers(); index++) {
                    Random r = new Random(returnedUnits.size() - 1);
                    int random = r.nextInt();
                    if (returnedUnits.get(random) < initialAttackers.get(random)) {
                        returnedUnits.set(random, returnedUnits.get(random) + 1);
                    } else {
                        index--;
                    }
                }
            }
            for (int index = 0; index < sources.size(); index++) {
                sources.get(index).setUnitCount(returnedUnits.get(index) + sources.get(index).getUnitCount());
            }
            attackers = 0;
            success = false;
            record.setSuccess(false);
            finished = true;
            record.setFinished(true);
            withdrawn = true;
            record.withdraw(returnedUnits);
        }

        public void clearBattlefield(){
            defenders = 0;
            initialAttackers.clear();
            attackers = 0;
            finished = false;
            success = false;
            withdrawn = false;
            sources.clear();
            cleared = true;
        }

}