/*
 * Pipeline.cpp
 *
 *  Created on: Apr 29, 2017
 *      Author: maazsaf
 */

#include "dag/Pipeline.h"

Dexter::Pipeline::Pipeline ()
{
  Stage * s = new Stage();
  this->head = s;
  this->currStage = s;
}

void Dexter::Pipeline::createNewStage ()
{
  Stage * s = new Stage();
  this->currStage->setNextStage(s);
  this->currStage = s;
}

void Dexter::Pipeline::addStage (Stage * s)
{
  std::queue<Stage*> stages;
  stages.push(this->head);
  while (!stages.empty())
  {
    Dexter::Stage * curr = stages.front();
    stages.pop();

    if (curr->forks())
    {
      stages.push(curr->getCons());
      stages.push(curr->getAltr());
    }
    else if (curr->getNextStage() != NULL)
    {
      stages.push(curr->getNextStage());
    }
    else if (curr != s)
    {
      curr->setNextStage(s);
    }
  }
}

void Dexter::Pipeline::createFork (clang::Expr * cond, Pipeline * cons, Pipeline * altr)
{
  Stage * s = new Stage();
  cons->addStage(s);
  altr->addStage(s);
  this->currStage->createFork(cond, cons->getHead(), altr->getHead());
  this->currStage->setNextStage(s);
  this->currStage = s;
}

Dexter::Stage* Dexter::Pipeline::getHead ()
{
  return this->head;
}

Dexter::Stage* Dexter::Pipeline::getCurrStage ()
{
  return this->currStage;
}

std::set<Dexter::Stage*> Dexter::Pipeline::getAllStages()
{
  std::set<Stage*> stages;

  std::queue<Stage*> pending;
  pending.push(this->head);
  while (!pending.empty())
  {
    Dexter::Stage * curr = pending.front();
    pending.pop();

    stages.insert(curr);

    if (curr->forks())
    {
      pending.push(curr->getCons());
      pending.push(curr->getAltr());
    }
    else if (curr->getNextStage() != NULL)
    {
      pending.push(curr->getNextStage());
    }
  }

  return stages;
}

void Dexter::Pipeline::simplify ()
{
  std::queue<Stage*> stages;
  stages.push(this->head);
  while (!stages.empty())
  {
    Dexter::Stage * curr = stages.front();
    stages.pop();

    if (curr->forks())
    {
      stages.push(curr->getCons());
      stages.push(curr->getAltr());
    }
    else if (curr->getNextStage() != NULL)
    {
      while (true) {
        Stage* nextStage = curr->getNextStage();
        if (nextStage->isEmpty() && !nextStage->forks() && nextStage->getNextStage() != NULL)
        {
          curr->setNextStage(nextStage->getNextStage());
        }
        else
          break;
      }


      stages.push(curr->getNextStage());
    }
  }
}

void Dexter::Pipeline::print ()
{
  std::queue<Stage*> stages;
  stages.push(this->head);
  while (!stages.empty())
  {
    Dexter::Stage * curr = stages.front();
    stages.pop();

    if (curr->isEmpty()) {
      llvm::outs() << "=====================================\n";
      llvm::outs() << "STAGE " << curr->getId() << " EMPTY \n";
      llvm::outs() << "=====================================\n";
    }
    else
    {
      llvm::outs() << "=====================================\n";
      llvm::outs() << "STAGE " << curr->getId() << "\n";
      llvm::outs() << "-------------------------------------\n";

      std::vector<Stmt*> stmts = curr->getStatements();
      std::vector<Stmt*>::iterator it;
      for (it = stmts.begin(); it != stmts.end(); ++it)
        llvm::outs() << Dexter::Util::print(*it) << "\n";

      llvm::outs() << "=====================================\n\n";
    }

    if (curr->forks())
    {
      stages.push(curr->getCons());
      stages.push(curr->getAltr());
    }
    else if (curr->getNextStage() != NULL)
    {
      stages.push(curr->getNextStage());
    }
  }
}