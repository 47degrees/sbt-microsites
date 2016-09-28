package kazari.model

case class EvaluatorRequest(resolvers: Seq[String], dependencies: Seq[EvaluatorDependency], code: String)
case class EvaluatorDependency(groupId: String, artifactId: String, version: String)