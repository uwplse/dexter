package dexter.dag;

import dexter.frontend.CodeAnalysis;
import dexter.ir.Expr;
import dexter.ir.parser.Util;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Pipeline {
  List<Stage> _stages;

  public Pipeline(JsonObject json) {
    this._stages = new ArrayList<>();

    JsonArray stages = json.getJsonArray("Stages");
    for (int i=0; i<stages.size(); i++) {
      JsonObject stage = stages.getJsonObject(i);

      int _id = stage.getInt("id");
      boolean _forks = stage.getBoolean("forks");
      boolean _containsLoop = stage.getBoolean("containsLoop");
      boolean _isEmpty = stage.getBoolean("isEmpty");

      Expr _cond;
      int _consStageId;
      int _altrStageId;
      int _nextStageId;

      if (_forks) {
        _cond = Util.parse(stage.getString("cond"), false);
        _consStageId = stage.getInt("cons");
        _altrStageId = stage.getInt("altr");
      }
      else {
        _cond = null;
        _consStageId = -1;
        _altrStageId = -1;
      }

      _nextStageId = stage.getInt("next");

      CodeAnalysis _analysis = (_isEmpty ? null : new CodeAnalysis(stage.getJsonObject("analysis")));

      this._stages.add(new Stage(_id, _forks, _containsLoop, _isEmpty, _cond, _consStageId, _altrStageId, _nextStageId, _analysis));
    }

    this._stages.sort(Comparator.comparingInt(s1 -> s1.id()));
  }

  public List<Stage> stages () { return _stages; };

  public Stage stage (int id) throws Exception {
    for (Stage s : _stages)
      if (s.id() == id)
        return s;

    throw new Exception("Stage with ID " + id + " not found");
  };
}