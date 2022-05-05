package iris.client_bff.iris_messages;

import iris.client_bff.core.AggregateRepository;
import iris.client_bff.iris_messages.IrisMessageData.IrisMessageDataIdentifier;

interface IrisMessageDataRepository extends AggregateRepository<IrisMessageData, IrisMessageDataIdentifier> {}
